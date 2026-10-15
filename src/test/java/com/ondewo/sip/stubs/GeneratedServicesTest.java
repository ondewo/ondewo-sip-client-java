package com.ondewo.sip.stubs;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import com.google.protobuf.Descriptors;
import com.ondewo.sip.auth.BearerToken;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import io.grpc.Server;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.ServerInterceptors;
import io.grpc.ServiceDescriptor;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ondewo.sip.SipGrpc;
import ondewo.sip.SipOuterClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Exercises the generated gRPC service stubs: their descriptors, every stub flavour, and one
 * real request/response round trip over the in-process transport - no socket, no network, but
 * the real generated marshallers on both ends.
 *
 * <p>Since ondewo-sip-api 5.5.0 the service also declares one bidirectional streaming rpc,
 * {@code SipStreamCallAudio}, whose method type is asserted below.
 */
class GeneratedServicesTest {

    /**
     * Services this client must always carry, whatever else the api adds: {@code ondewo.sip.Sip}.
     * Everything else is DERIVED - from the protos of the ondewo-sip-api submodule when it is
     * checked out, and from the descriptors of the generated outer classes always - so an api
     * release that adds or drops a service needs no edit here.
     */
    private static final List<String> REQUIRED_SERVICES = List.of("ondewo.sip.Sip");

    /** The proto tree the stubs are generated from (the ondewo-sip-api submodule). */
    private static final Path PROTO_ROOT = Paths.get("ondewo-sip-api", "ondewo");

    private static final Pattern PACKAGE = Pattern.compile("\\bpackage\\s+([\\w.]+)\\s*;");
    private static final Pattern SERVICE = Pattern.compile("\\bservice\\s+(\\w+)\\s*\\{");
    private static final Pattern RPC = Pattern.compile("\\brpc\\s+(\\w+)\\s*\\(");

    private static final Metadata.Key<String> AUTHORIZATION =
            Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER);

    private final AtomicReference<Metadata> receivedHeaders = new AtomicReference<>();

    private Server server;
    private ManagedChannel channel;

    @BeforeEach
    void startServer() throws Exception {
        final SipGrpc.SipImplBase service =
                new SipGrpc.SipImplBase() {
                    @Override
                    public void sipStartSession(
                            final SipOuterClass.SipStartSessionRequest request,
                            final StreamObserver<SipOuterClass.SipStatus> responseObserver) {
                        responseObserver.onNext(
                                SipOuterClass.SipStatus.newBuilder()
                                        .setAccountName(request.getAccountName())
                                        .setStatusType(
                                                SipOuterClass.SipStatus.StatusType.SESSION_STARTED)
                                        .setDescription(
                                                "auto answer after "
                                                        + request.getAutoAnswerInterval()
                                                        + "s")
                                        .build());
                        responseObserver.onCompleted();
                    }
                };

        final ServerInterceptor headerCapture =
                new ServerInterceptor() {
                    @Override
                    public <Q, S> ServerCall.Listener<Q> interceptCall(
                            final ServerCall<Q, S> call,
                            final Metadata headers,
                            final ServerCallHandler<Q, S> next) {
                        receivedHeaders.set(headers);
                        return next.startCall(call, headers);
                    }
                };

        final String name = InProcessServerBuilder.generateName();
        server =
                InProcessServerBuilder.forName(name)
                        .directExecutor()
                        .addService(ServerInterceptors.intercept(service, headerCapture))
                        .build()
                        .start();
        channel = InProcessChannelBuilder.forName(name).build();
    }

    @AfterEach
    void stopServer() throws Exception {
        channel.shutdownNow();
        server.shutdownNow();
        channel.awaitTermination(10, TimeUnit.SECONDS);
        server.awaitTermination(10, TimeUnit.SECONDS);
    }

    @Test
    void exposesTheExpectedServiceDescriptor() {
        final ServiceDescriptor descriptor = SipGrpc.getServiceDescriptor();

        final List<String> methods =
                descriptor.getMethods().stream()
                        .map(MethodDescriptor::getBareMethodName)
                        .collect(toList());

        assertEquals("ondewo.sip.Sip", descriptor.getName());
        assertTrue(
                methods.containsAll(
                        List.of(
                                "SipStartSession",
                                "SipEndSession",
                                "SipStartCall",
                                "SipEndCall",
                                "SipRegisterAccount",
                                "SipReportAnsweringMachineDetected",
                                "SipSetCallMediaControl",
                                "SipStreamCallAudio")),
                "missing rpcs, got " + methods);
        assertEquals(
                MethodDescriptor.MethodType.UNARY,
                SipGrpc.getSipStartSessionMethod().getType());
        assertEquals(
                "ondewo.sip.Sip/SipStartSession",
                SipGrpc.getSipStartSessionMethod().getFullMethodName());
        assertEquals(
                MethodDescriptor.MethodType.BIDI_STREAMING,
                SipGrpc.getSipStreamCallAudioMethod().getType());
    }

    /** The compiled classes of the generated stubs. */
    private static Path classesRoot() throws Exception {
        final Path root =
                Paths.get(SipGrpc.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        assertTrue(Files.isDirectory(root), "expected compiled classes at " + root);
        return root;
    }

    /** The binary names of every top-level class below the classes root. */
    private static List<String> topLevelClasses(final Path root) throws Exception {
        try (Stream<Path> tree = Files.walk(root)) {
            return tree.filter(Files::isRegularFile)
                    .map(path -> root.relativize(path).toString())
                    .filter(name -> name.endsWith(".class") && !name.contains("$"))
                    .map(name -> name.substring(0, name.length() - ".class".length()))
                    .map(name -> name.replace(java.io.File.separatorChar, '.'))
                    .sorted()
                    .collect(toList());
        }
    }

    private static Set<String> rpcNames(final ServiceDescriptor descriptor) {
        return descriptor.getMethods().stream()
                .map(MethodDescriptor::getBareMethodName)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /** Service -> rpcs, as the generated {@code *Grpc} classes serve them. */
    private static Map<String, Set<String>> servicesOfTheGrpcClasses() throws Exception {
        final Map<String, Set<String>> services = new TreeMap<>();
        for (final String className : topLevelClasses(classesRoot())) {
            if (className.endsWith("Grpc")) {
                final ServiceDescriptor descriptor =
                        (ServiceDescriptor)
                                Class.forName(className).getMethod("getServiceDescriptor").invoke(null);
                assertEquals(
                        null,
                        services.put(descriptor.getName(), rpcNames(descriptor)),
                        "two classes serve " + descriptor.getName());
            }
        }
        return services;
    }

    /**
     * Service -> rpcs, as the file descriptors of the generated outer classes declare them: protoc
     * emits one outer class per .proto, with a static {@code getDescriptor()} that returns its
     * {@link Descriptors.FileDescriptor}, whether or not the gRPC plugin ran for that file.
     */
    private static Map<String, Set<String>> servicesOfTheFileDescriptors() throws Exception {
        final Map<String, Set<String>> services = new TreeMap<>();
        for (final String className : topLevelClasses(classesRoot())) {
            if (className.endsWith("Grpc")) {
                continue;
            }
            final Method getDescriptor;
            try {
                getDescriptor = Class.forName(className).getMethod("getDescriptor");
            } catch (final NoSuchMethodException notAnOuterClass) {
                continue;
            }
            if (!Modifier.isStatic(getDescriptor.getModifiers())
                    || getDescriptor.getReturnType() != Descriptors.FileDescriptor.class) {
                continue;
            }
            final Descriptors.FileDescriptor file =
                    (Descriptors.FileDescriptor) getDescriptor.invoke(null);
            for (final Descriptors.ServiceDescriptor service : file.getServices()) {
                services.put(
                        service.getFullName(),
                        service.getMethods().stream()
                                .map(Descriptors.MethodDescriptor::getName)
                                .collect(Collectors.toCollection(TreeSet::new)));
            }
        }
        return services;
    }

    /**
     * The proto source with comments and string literals blanked out, in ONE pass: an http option
     * such as {@code "/v2/{parent=projects/*}/agent"} opens a block comment for any regex that
     * strips comments before strings, and a {@code //} in a comment can hide a quote.
     */
    private static String withoutCommentsAndStrings(final String source) {
        final StringBuilder out = new StringBuilder(source.length());
        int i = 0;
        while (i < source.length()) {
            final char c = source.charAt(i);
            if (source.startsWith("//", i)) {
                while (i < source.length() && source.charAt(i) != '\n') {
                    i++;
                }
            } else if (source.startsWith("/*", i)) {
                final int end = source.indexOf("*/", i + 2);
                i = end < 0 ? source.length() : end + 2;
                out.append(' ');
            } else if (c == '"' || c == '\'') {
                i++;
                while (i < source.length() && source.charAt(i) != c) {
                    i += source.charAt(i) == '\\' ? 2 : 1;
                }
                i++;
                out.append("\"\"");
            } else {
                out.append(c);
                i++;
            }
        }
        return out.toString();
    }

    /** Service -> rpcs, read from the {@code service} / {@code rpc} lines of the .proto files. */
    private static Map<String, Set<String>> servicesOfTheProtos() throws Exception {
        final List<Path> protos;
        try (Stream<Path> tree = Files.walk(PROTO_ROOT)) {
            protos =
                    tree.filter(path -> path.toString().endsWith(".proto"))
                            .sorted()
                            .collect(toList());
        }
        final Map<String, Set<String>> services = new TreeMap<>();
        for (final Path proto : protos) {
            final String text =
                    withoutCommentsAndStrings(
                            new String(Files.readAllBytes(proto), StandardCharsets.UTF_8));
            final Matcher pkg = PACKAGE.matcher(text);
            final String prefix = pkg.find() ? pkg.group(1) + "." : "";
            final Matcher service = SERVICE.matcher(text);
            while (service.find()) {
                int depth = 1;
                int end = service.end();
                while (depth > 0 && end < text.length()) {
                    final char c = text.charAt(end++);
                    depth += c == '{' ? 1 : c == '}' ? -1 : 0;
                }
                final Set<String> rpcs = new TreeSet<>();
                final Matcher rpc = RPC.matcher(text.substring(service.end(), end));
                while (rpc.find()) {
                    rpcs.add(rpc.group(1));
                }
                services.put(prefix + service.group(1), rpcs);
            }
        }
        return services;
    }

    /**
     * Every service of every compiled proto has a generated {@code *Grpc} class with exactly the
     * rpcs its descriptor declares, and every one is usable. Nothing is counted or listed by hand.
     */
    @Test
    void everyServiceOfTheCompiledProtosHasAGeneratedStub() throws Exception {
        final Map<String, Set<String>> stubs = servicesOfTheGrpcClasses();

        assertEquals(servicesOfTheFileDescriptors(), stubs);
        assertTrue(stubs.keySet().containsAll(REQUIRED_SERVICES), "found " + stubs.keySet());
        for (final Map.Entry<String, Set<String>> service : stubs.entrySet()) {
            assertTrue(service.getKey().startsWith("ondewo."), "unexpected service " + service.getKey());
            assertFalse(service.getValue().isEmpty(), service.getKey() + " declares no rpc");
        }
    }

    /**
     * The committed stubs serve exactly the services and rpcs of the checked-out api protos. CI
     * checks the submodule out; without it (e.g. a plain {@code mvn verify} on a fresh clone) this
     * is skipped and the descriptor-derived test above still runs.
     */
    @Test
    void theStubsServeExactlyTheServicesAndRpcsOfTheApiProtos() throws Exception {
        assumeTrue(Files.isDirectory(PROTO_ROOT), "the ondewo-sip-api submodule is not checked out");

        final Map<String, Set<String>> protos = servicesOfTheProtos();

        assertTrue(protos.keySet().containsAll(REQUIRED_SERVICES), "parsed " + protos.keySet());
        assertEquals(protos, servicesOfTheGrpcClasses());
    }

    @Test
    void servesAUnaryCallOverTheGeneratedMarshallers() {
        final SipGrpc.SipBlockingStub stub =
                new BearerToken("s3cr3t").attachTo(SipGrpc.newBlockingStub(channel));

        final SipOuterClass.SipStatus status =
                stub.sipStartSession(
                        SipOuterClass.SipStartSessionRequest.newBuilder()
                                .setAccountName("sip-user-1@mydomain.com")
                                .setAutoAnswerInterval(3)
                                .build());

        assertEquals("sip-user-1@mydomain.com", status.getAccountName());
        assertEquals(SipOuterClass.SipStatus.StatusType.SESSION_STARTED, status.getStatusType());
        assertEquals("auto answer after 3s", status.getDescription());
        assertEquals("Bearer s3cr3t", receivedHeaders.get().get(AUTHORIZATION));
    }

    /**
     * The published library declares grpc-netty-shaded, so a consumer can open a channel from
     * a plain target string without adding a transport. Nothing is dialled: gRPC connects
     * lazily, on the first call.
     */
    @Test
    void buildsEveryStubFlavourAgainstAPlainTargetChannel() {
        final ManagedChannel dummy =
                ManagedChannelBuilder.forTarget("localhost:50051").usePlaintext().build();
        try {
            assertNotNull(SipGrpc.newBlockingStub(dummy));
            assertNotNull(SipGrpc.newFutureStub(dummy));
            assertNotNull(SipGrpc.newStub(dummy));
        } finally {
            dummy.shutdownNow();
        }
    }
}
