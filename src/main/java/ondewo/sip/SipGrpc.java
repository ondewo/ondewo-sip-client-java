package ondewo.sip;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * &lt;p&gt;ONDEWO-SIP API available at &lt;a href="https://github.com/ondewo/ondewo-sip-api"&gt;GitHub&lt;/a&gt;&lt;/p&gt;
 * </pre>
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class SipGrpc {

  private SipGrpc() {}

  public static final java.lang.String SERVICE_NAME = "ondewo.sip.Sip";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipStartSessionRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipStartSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipStartSession",
      requestType = ondewo.sip.SipOuterClass.SipStartSessionRequest.class,
      responseType = ondewo.sip.SipOuterClass.SipStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipStartSessionRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipStartSessionMethod() {
    io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipStartSessionRequest, ondewo.sip.SipOuterClass.SipStatus> getSipStartSessionMethod;
    if ((getSipStartSessionMethod = SipGrpc.getSipStartSessionMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipStartSessionMethod = SipGrpc.getSipStartSessionMethod) == null) {
          SipGrpc.getSipStartSessionMethod = getSipStartSessionMethod =
              io.grpc.MethodDescriptor.<ondewo.sip.SipOuterClass.SipStartSessionRequest, ondewo.sip.SipOuterClass.SipStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipStartSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStartSessionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatus.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipStartSession"))
              .build();
        }
      }
    }
    return getSipStartSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ondewo.sip.SipOuterClass.SipStatus> getSipEndSessionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipEndSession",
      requestType = com.google.protobuf.Empty.class,
      responseType = ondewo.sip.SipOuterClass.SipStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ondewo.sip.SipOuterClass.SipStatus> getSipEndSessionMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, ondewo.sip.SipOuterClass.SipStatus> getSipEndSessionMethod;
    if ((getSipEndSessionMethod = SipGrpc.getSipEndSessionMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipEndSessionMethod = SipGrpc.getSipEndSessionMethod) == null) {
          SipGrpc.getSipEndSessionMethod = getSipEndSessionMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, ondewo.sip.SipOuterClass.SipStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipEndSession"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatus.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipEndSession"))
              .build();
        }
      }
    }
    return getSipEndSessionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipStartCallRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipStartCallMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipStartCall",
      requestType = ondewo.sip.SipOuterClass.SipStartCallRequest.class,
      responseType = ondewo.sip.SipOuterClass.SipStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipStartCallRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipStartCallMethod() {
    io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipStartCallRequest, ondewo.sip.SipOuterClass.SipStatus> getSipStartCallMethod;
    if ((getSipStartCallMethod = SipGrpc.getSipStartCallMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipStartCallMethod = SipGrpc.getSipStartCallMethod) == null) {
          SipGrpc.getSipStartCallMethod = getSipStartCallMethod =
              io.grpc.MethodDescriptor.<ondewo.sip.SipOuterClass.SipStartCallRequest, ondewo.sip.SipOuterClass.SipStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipStartCall"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStartCallRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatus.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipStartCall"))
              .build();
        }
      }
    }
    return getSipStartCallMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipEndCallRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipEndCallMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipEndCall",
      requestType = ondewo.sip.SipOuterClass.SipEndCallRequest.class,
      responseType = ondewo.sip.SipOuterClass.SipStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipEndCallRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipEndCallMethod() {
    io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipEndCallRequest, ondewo.sip.SipOuterClass.SipStatus> getSipEndCallMethod;
    if ((getSipEndCallMethod = SipGrpc.getSipEndCallMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipEndCallMethod = SipGrpc.getSipEndCallMethod) == null) {
          SipGrpc.getSipEndCallMethod = getSipEndCallMethod =
              io.grpc.MethodDescriptor.<ondewo.sip.SipOuterClass.SipEndCallRequest, ondewo.sip.SipOuterClass.SipStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipEndCall"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipEndCallRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatus.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipEndCall"))
              .build();
        }
      }
    }
    return getSipEndCallMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipTransferCallRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipTransferCallMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipTransferCall",
      requestType = ondewo.sip.SipOuterClass.SipTransferCallRequest.class,
      responseType = ondewo.sip.SipOuterClass.SipStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipTransferCallRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipTransferCallMethod() {
    io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipTransferCallRequest, ondewo.sip.SipOuterClass.SipStatus> getSipTransferCallMethod;
    if ((getSipTransferCallMethod = SipGrpc.getSipTransferCallMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipTransferCallMethod = SipGrpc.getSipTransferCallMethod) == null) {
          SipGrpc.getSipTransferCallMethod = getSipTransferCallMethod =
              io.grpc.MethodDescriptor.<ondewo.sip.SipOuterClass.SipTransferCallRequest, ondewo.sip.SipOuterClass.SipStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipTransferCall"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipTransferCallRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatus.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipTransferCall"))
              .build();
        }
      }
    }
    return getSipTransferCallMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipRegisterAccountRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipRegisterAccountMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipRegisterAccount",
      requestType = ondewo.sip.SipOuterClass.SipRegisterAccountRequest.class,
      responseType = ondewo.sip.SipOuterClass.SipStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipRegisterAccountRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipRegisterAccountMethod() {
    io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipRegisterAccountRequest, ondewo.sip.SipOuterClass.SipStatus> getSipRegisterAccountMethod;
    if ((getSipRegisterAccountMethod = SipGrpc.getSipRegisterAccountMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipRegisterAccountMethod = SipGrpc.getSipRegisterAccountMethod) == null) {
          SipGrpc.getSipRegisterAccountMethod = getSipRegisterAccountMethod =
              io.grpc.MethodDescriptor.<ondewo.sip.SipOuterClass.SipRegisterAccountRequest, ondewo.sip.SipOuterClass.SipStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipRegisterAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipRegisterAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatus.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipRegisterAccount"))
              .build();
        }
      }
    }
    return getSipRegisterAccountMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ondewo.sip.SipOuterClass.SipStatus> getSipGetSipStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipGetSipStatus",
      requestType = com.google.protobuf.Empty.class,
      responseType = ondewo.sip.SipOuterClass.SipStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ondewo.sip.SipOuterClass.SipStatus> getSipGetSipStatusMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, ondewo.sip.SipOuterClass.SipStatus> getSipGetSipStatusMethod;
    if ((getSipGetSipStatusMethod = SipGrpc.getSipGetSipStatusMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipGetSipStatusMethod = SipGrpc.getSipGetSipStatusMethod) == null) {
          SipGrpc.getSipGetSipStatusMethod = getSipGetSipStatusMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, ondewo.sip.SipOuterClass.SipStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipGetSipStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatus.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipGetSipStatus"))
              .build();
        }
      }
    }
    return getSipGetSipStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ondewo.sip.SipOuterClass.SipStatusHistoryResponse> getSipGetSipStatusHistoryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipGetSipStatusHistory",
      requestType = com.google.protobuf.Empty.class,
      responseType = ondewo.sip.SipOuterClass.SipStatusHistoryResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ondewo.sip.SipOuterClass.SipStatusHistoryResponse> getSipGetSipStatusHistoryMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, ondewo.sip.SipOuterClass.SipStatusHistoryResponse> getSipGetSipStatusHistoryMethod;
    if ((getSipGetSipStatusHistoryMethod = SipGrpc.getSipGetSipStatusHistoryMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipGetSipStatusHistoryMethod = SipGrpc.getSipGetSipStatusHistoryMethod) == null) {
          SipGrpc.getSipGetSipStatusHistoryMethod = getSipGetSipStatusHistoryMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, ondewo.sip.SipOuterClass.SipStatusHistoryResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipGetSipStatusHistory"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatusHistoryResponse.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipGetSipStatusHistory"))
              .build();
        }
      }
    }
    return getSipGetSipStatusHistoryMethod;
  }

  private static volatile io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipPlayWavFilesRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipPlayWavFilesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipPlayWavFiles",
      requestType = ondewo.sip.SipOuterClass.SipPlayWavFilesRequest.class,
      responseType = ondewo.sip.SipOuterClass.SipStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipPlayWavFilesRequest,
      ondewo.sip.SipOuterClass.SipStatus> getSipPlayWavFilesMethod() {
    io.grpc.MethodDescriptor<ondewo.sip.SipOuterClass.SipPlayWavFilesRequest, ondewo.sip.SipOuterClass.SipStatus> getSipPlayWavFilesMethod;
    if ((getSipPlayWavFilesMethod = SipGrpc.getSipPlayWavFilesMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipPlayWavFilesMethod = SipGrpc.getSipPlayWavFilesMethod) == null) {
          SipGrpc.getSipPlayWavFilesMethod = getSipPlayWavFilesMethod =
              io.grpc.MethodDescriptor.<ondewo.sip.SipOuterClass.SipPlayWavFilesRequest, ondewo.sip.SipOuterClass.SipStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipPlayWavFiles"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipPlayWavFilesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatus.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipPlayWavFiles"))
              .build();
        }
      }
    }
    return getSipPlayWavFilesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ondewo.sip.SipOuterClass.SipStatus> getSipMuteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipMute",
      requestType = com.google.protobuf.Empty.class,
      responseType = ondewo.sip.SipOuterClass.SipStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ondewo.sip.SipOuterClass.SipStatus> getSipMuteMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, ondewo.sip.SipOuterClass.SipStatus> getSipMuteMethod;
    if ((getSipMuteMethod = SipGrpc.getSipMuteMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipMuteMethod = SipGrpc.getSipMuteMethod) == null) {
          SipGrpc.getSipMuteMethod = getSipMuteMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, ondewo.sip.SipOuterClass.SipStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipMute"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatus.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipMute"))
              .build();
        }
      }
    }
    return getSipMuteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ondewo.sip.SipOuterClass.SipStatus> getSipUnMuteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SipUnMute",
      requestType = com.google.protobuf.Empty.class,
      responseType = ondewo.sip.SipOuterClass.SipStatus.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      ondewo.sip.SipOuterClass.SipStatus> getSipUnMuteMethod() {
    io.grpc.MethodDescriptor<com.google.protobuf.Empty, ondewo.sip.SipOuterClass.SipStatus> getSipUnMuteMethod;
    if ((getSipUnMuteMethod = SipGrpc.getSipUnMuteMethod) == null) {
      synchronized (SipGrpc.class) {
        if ((getSipUnMuteMethod = SipGrpc.getSipUnMuteMethod) == null) {
          SipGrpc.getSipUnMuteMethod = getSipUnMuteMethod =
              io.grpc.MethodDescriptor.<com.google.protobuf.Empty, ondewo.sip.SipOuterClass.SipStatus>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SipUnMute"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.google.protobuf.Empty.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  ondewo.sip.SipOuterClass.SipStatus.getDefaultInstance()))
              .setSchemaDescriptor(new SipMethodDescriptorSupplier("SipUnMute"))
              .build();
        }
      }
    }
    return getSipUnMuteMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static SipStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SipStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SipStub>() {
        @java.lang.Override
        public SipStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SipStub(channel, callOptions);
        }
      };
    return SipStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static SipBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SipBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SipBlockingV2Stub>() {
        @java.lang.Override
        public SipBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SipBlockingV2Stub(channel, callOptions);
        }
      };
    return SipBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static SipBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SipBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SipBlockingStub>() {
        @java.lang.Override
        public SipBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SipBlockingStub(channel, callOptions);
        }
      };
    return SipBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static SipFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<SipFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<SipFutureStub>() {
        @java.lang.Override
        public SipFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new SipFutureStub(channel, callOptions);
        }
      };
    return SipFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * &lt;p&gt;ONDEWO-SIP API available at &lt;a href="https://github.com/ondewo/ondewo-sip-api"&gt;GitHub&lt;/a&gt;&lt;/p&gt;
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * &lt;p&gt;Starts a new SIP session for an account registered at a SIP server. &lt;code&gt;RegisterAccount&lt;/code&gt; need to be called before.&lt;/p&gt;
     * </pre>
     */
    default void sipStartSession(ondewo.sip.SipOuterClass.SipStartSessionRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipStartSessionMethod(), responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Ends a SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    default void sipEndSession(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipEndSessionMethod(), responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Starts a call in an active SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    default void sipStartCall(ondewo.sip.SipOuterClass.SipStartCallRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipStartCallMethod(), responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Ends a call in an active SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    default void sipEndCall(ondewo.sip.SipOuterClass.SipEndCallRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipEndCallMethod(), responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Transfers a call in an active SIP session for an account registered at a SIP server to another SIP account or phone number specified by &lt;code&gt;transfer_id&lt;/code&gt;&lt;/p&gt;
     * </pre>
     */
    default void sipTransferCall(ondewo.sip.SipOuterClass.SipTransferCallRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipTransferCallMethod(), responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Registers s SIP account at a SIP server&lt;/p&gt;
     * </pre>
     */
    default void sipRegisterAccount(ondewo.sip.SipOuterClass.SipRegisterAccountRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipRegisterAccountMethod(), responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Gets the current SIP status&lt;/p&gt;
     * </pre>
     */
    default void sipGetSipStatus(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipGetSipStatusMethod(), responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Gets the history of SIP status&lt;/p&gt;
     * </pre>
     */
    default void sipGetSipStatusHistory(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatusHistoryResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipGetSipStatusHistoryMethod(), responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Plays wav files during an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    default void sipPlayWavFiles(ondewo.sip.SipOuterClass.SipPlayWavFilesRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipPlayWavFilesMethod(), responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Mutes the microphone in an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    default void sipMute(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipMuteMethod(), responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Un-mutes the microphone in an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    default void sipUnMute(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSipUnMuteMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Sip.
   * <pre>
   * &lt;p&gt;ONDEWO-SIP API available at &lt;a href="https://github.com/ondewo/ondewo-sip-api"&gt;GitHub&lt;/a&gt;&lt;/p&gt;
   * </pre>
   */
  public static abstract class SipImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return SipGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Sip.
   * <pre>
   * &lt;p&gt;ONDEWO-SIP API available at &lt;a href="https://github.com/ondewo/ondewo-sip-api"&gt;GitHub&lt;/a&gt;&lt;/p&gt;
   * </pre>
   */
  public static final class SipStub
      extends io.grpc.stub.AbstractAsyncStub<SipStub> {
    private SipStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SipStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SipStub(channel, callOptions);
    }

    /**
     * <pre>
     * &lt;p&gt;Starts a new SIP session for an account registered at a SIP server. &lt;code&gt;RegisterAccount&lt;/code&gt; need to be called before.&lt;/p&gt;
     * </pre>
     */
    public void sipStartSession(ondewo.sip.SipOuterClass.SipStartSessionRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipStartSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Ends a SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public void sipEndSession(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipEndSessionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Starts a call in an active SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public void sipStartCall(ondewo.sip.SipOuterClass.SipStartCallRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipStartCallMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Ends a call in an active SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public void sipEndCall(ondewo.sip.SipOuterClass.SipEndCallRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipEndCallMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Transfers a call in an active SIP session for an account registered at a SIP server to another SIP account or phone number specified by &lt;code&gt;transfer_id&lt;/code&gt;&lt;/p&gt;
     * </pre>
     */
    public void sipTransferCall(ondewo.sip.SipOuterClass.SipTransferCallRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipTransferCallMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Registers s SIP account at a SIP server&lt;/p&gt;
     * </pre>
     */
    public void sipRegisterAccount(ondewo.sip.SipOuterClass.SipRegisterAccountRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipRegisterAccountMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Gets the current SIP status&lt;/p&gt;
     * </pre>
     */
    public void sipGetSipStatus(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipGetSipStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Gets the history of SIP status&lt;/p&gt;
     * </pre>
     */
    public void sipGetSipStatusHistory(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatusHistoryResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipGetSipStatusHistoryMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Plays wav files during an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public void sipPlayWavFiles(ondewo.sip.SipOuterClass.SipPlayWavFilesRequest request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipPlayWavFilesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Mutes the microphone in an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public void sipMute(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipMuteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * &lt;p&gt;Un-mutes the microphone in an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public void sipUnMute(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSipUnMuteMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Sip.
   * <pre>
   * &lt;p&gt;ONDEWO-SIP API available at &lt;a href="https://github.com/ondewo/ondewo-sip-api"&gt;GitHub&lt;/a&gt;&lt;/p&gt;
   * </pre>
   */
  public static final class SipBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<SipBlockingV2Stub> {
    private SipBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SipBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SipBlockingV2Stub(channel, callOptions);
    }

    /**
     * <pre>
     * &lt;p&gt;Starts a new SIP session for an account registered at a SIP server. &lt;code&gt;RegisterAccount&lt;/code&gt; need to be called before.&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipStartSession(ondewo.sip.SipOuterClass.SipStartSessionRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipStartSessionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Ends a SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipEndSession(com.google.protobuf.Empty request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipEndSessionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Starts a call in an active SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipStartCall(ondewo.sip.SipOuterClass.SipStartCallRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipStartCallMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Ends a call in an active SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipEndCall(ondewo.sip.SipOuterClass.SipEndCallRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipEndCallMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Transfers a call in an active SIP session for an account registered at a SIP server to another SIP account or phone number specified by &lt;code&gt;transfer_id&lt;/code&gt;&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipTransferCall(ondewo.sip.SipOuterClass.SipTransferCallRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipTransferCallMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Registers s SIP account at a SIP server&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipRegisterAccount(ondewo.sip.SipOuterClass.SipRegisterAccountRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipRegisterAccountMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Gets the current SIP status&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipGetSipStatus(com.google.protobuf.Empty request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipGetSipStatusMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Gets the history of SIP status&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatusHistoryResponse sipGetSipStatusHistory(com.google.protobuf.Empty request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipGetSipStatusHistoryMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Plays wav files during an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipPlayWavFiles(ondewo.sip.SipOuterClass.SipPlayWavFilesRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipPlayWavFilesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Mutes the microphone in an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipMute(com.google.protobuf.Empty request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipMuteMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Un-mutes the microphone in an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipUnMute(com.google.protobuf.Empty request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getSipUnMuteMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service Sip.
   * <pre>
   * &lt;p&gt;ONDEWO-SIP API available at &lt;a href="https://github.com/ondewo/ondewo-sip-api"&gt;GitHub&lt;/a&gt;&lt;/p&gt;
   * </pre>
   */
  public static final class SipBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<SipBlockingStub> {
    private SipBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SipBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SipBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * &lt;p&gt;Starts a new SIP session for an account registered at a SIP server. &lt;code&gt;RegisterAccount&lt;/code&gt; need to be called before.&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipStartSession(ondewo.sip.SipOuterClass.SipStartSessionRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipStartSessionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Ends a SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipEndSession(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipEndSessionMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Starts a call in an active SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipStartCall(ondewo.sip.SipOuterClass.SipStartCallRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipStartCallMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Ends a call in an active SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipEndCall(ondewo.sip.SipOuterClass.SipEndCallRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipEndCallMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Transfers a call in an active SIP session for an account registered at a SIP server to another SIP account or phone number specified by &lt;code&gt;transfer_id&lt;/code&gt;&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipTransferCall(ondewo.sip.SipOuterClass.SipTransferCallRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipTransferCallMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Registers s SIP account at a SIP server&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipRegisterAccount(ondewo.sip.SipOuterClass.SipRegisterAccountRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipRegisterAccountMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Gets the current SIP status&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipGetSipStatus(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipGetSipStatusMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Gets the history of SIP status&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatusHistoryResponse sipGetSipStatusHistory(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipGetSipStatusHistoryMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Plays wav files during an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipPlayWavFiles(ondewo.sip.SipOuterClass.SipPlayWavFilesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipPlayWavFilesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Mutes the microphone in an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipMute(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipMuteMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Un-mutes the microphone in an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public ondewo.sip.SipOuterClass.SipStatus sipUnMute(com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSipUnMuteMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Sip.
   * <pre>
   * &lt;p&gt;ONDEWO-SIP API available at &lt;a href="https://github.com/ondewo/ondewo-sip-api"&gt;GitHub&lt;/a&gt;&lt;/p&gt;
   * </pre>
   */
  public static final class SipFutureStub
      extends io.grpc.stub.AbstractFutureStub<SipFutureStub> {
    private SipFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected SipFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new SipFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * &lt;p&gt;Starts a new SIP session for an account registered at a SIP server. &lt;code&gt;RegisterAccount&lt;/code&gt; need to be called before.&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatus> sipStartSession(
        ondewo.sip.SipOuterClass.SipStartSessionRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipStartSessionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Ends a SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatus> sipEndSession(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipEndSessionMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Starts a call in an active SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatus> sipStartCall(
        ondewo.sip.SipOuterClass.SipStartCallRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipStartCallMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Ends a call in an active SIP session for an account registered at a SIP server&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatus> sipEndCall(
        ondewo.sip.SipOuterClass.SipEndCallRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipEndCallMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Transfers a call in an active SIP session for an account registered at a SIP server to another SIP account or phone number specified by &lt;code&gt;transfer_id&lt;/code&gt;&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatus> sipTransferCall(
        ondewo.sip.SipOuterClass.SipTransferCallRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipTransferCallMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Registers s SIP account at a SIP server&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatus> sipRegisterAccount(
        ondewo.sip.SipOuterClass.SipRegisterAccountRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipRegisterAccountMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Gets the current SIP status&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatus> sipGetSipStatus(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipGetSipStatusMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Gets the history of SIP status&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatusHistoryResponse> sipGetSipStatusHistory(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipGetSipStatusHistoryMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Plays wav files during an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatus> sipPlayWavFiles(
        ondewo.sip.SipOuterClass.SipPlayWavFilesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipPlayWavFilesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Mutes the microphone in an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatus> sipMute(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipMuteMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * &lt;p&gt;Un-mutes the microphone in an ongoing call of an active SIP session&lt;/p&gt;
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<ondewo.sip.SipOuterClass.SipStatus> sipUnMute(
        com.google.protobuf.Empty request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSipUnMuteMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SIP_START_SESSION = 0;
  private static final int METHODID_SIP_END_SESSION = 1;
  private static final int METHODID_SIP_START_CALL = 2;
  private static final int METHODID_SIP_END_CALL = 3;
  private static final int METHODID_SIP_TRANSFER_CALL = 4;
  private static final int METHODID_SIP_REGISTER_ACCOUNT = 5;
  private static final int METHODID_SIP_GET_SIP_STATUS = 6;
  private static final int METHODID_SIP_GET_SIP_STATUS_HISTORY = 7;
  private static final int METHODID_SIP_PLAY_WAV_FILES = 8;
  private static final int METHODID_SIP_MUTE = 9;
  private static final int METHODID_SIP_UN_MUTE = 10;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SIP_START_SESSION:
          serviceImpl.sipStartSession((ondewo.sip.SipOuterClass.SipStartSessionRequest) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus>) responseObserver);
          break;
        case METHODID_SIP_END_SESSION:
          serviceImpl.sipEndSession((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus>) responseObserver);
          break;
        case METHODID_SIP_START_CALL:
          serviceImpl.sipStartCall((ondewo.sip.SipOuterClass.SipStartCallRequest) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus>) responseObserver);
          break;
        case METHODID_SIP_END_CALL:
          serviceImpl.sipEndCall((ondewo.sip.SipOuterClass.SipEndCallRequest) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus>) responseObserver);
          break;
        case METHODID_SIP_TRANSFER_CALL:
          serviceImpl.sipTransferCall((ondewo.sip.SipOuterClass.SipTransferCallRequest) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus>) responseObserver);
          break;
        case METHODID_SIP_REGISTER_ACCOUNT:
          serviceImpl.sipRegisterAccount((ondewo.sip.SipOuterClass.SipRegisterAccountRequest) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus>) responseObserver);
          break;
        case METHODID_SIP_GET_SIP_STATUS:
          serviceImpl.sipGetSipStatus((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus>) responseObserver);
          break;
        case METHODID_SIP_GET_SIP_STATUS_HISTORY:
          serviceImpl.sipGetSipStatusHistory((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatusHistoryResponse>) responseObserver);
          break;
        case METHODID_SIP_PLAY_WAV_FILES:
          serviceImpl.sipPlayWavFiles((ondewo.sip.SipOuterClass.SipPlayWavFilesRequest) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus>) responseObserver);
          break;
        case METHODID_SIP_MUTE:
          serviceImpl.sipMute((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus>) responseObserver);
          break;
        case METHODID_SIP_UN_MUTE:
          serviceImpl.sipUnMute((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<ondewo.sip.SipOuterClass.SipStatus>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSipStartSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ondewo.sip.SipOuterClass.SipStartSessionRequest,
              ondewo.sip.SipOuterClass.SipStatus>(
                service, METHODID_SIP_START_SESSION)))
        .addMethod(
          getSipEndSessionMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              ondewo.sip.SipOuterClass.SipStatus>(
                service, METHODID_SIP_END_SESSION)))
        .addMethod(
          getSipStartCallMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ondewo.sip.SipOuterClass.SipStartCallRequest,
              ondewo.sip.SipOuterClass.SipStatus>(
                service, METHODID_SIP_START_CALL)))
        .addMethod(
          getSipEndCallMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ondewo.sip.SipOuterClass.SipEndCallRequest,
              ondewo.sip.SipOuterClass.SipStatus>(
                service, METHODID_SIP_END_CALL)))
        .addMethod(
          getSipTransferCallMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ondewo.sip.SipOuterClass.SipTransferCallRequest,
              ondewo.sip.SipOuterClass.SipStatus>(
                service, METHODID_SIP_TRANSFER_CALL)))
        .addMethod(
          getSipRegisterAccountMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ondewo.sip.SipOuterClass.SipRegisterAccountRequest,
              ondewo.sip.SipOuterClass.SipStatus>(
                service, METHODID_SIP_REGISTER_ACCOUNT)))
        .addMethod(
          getSipGetSipStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              ondewo.sip.SipOuterClass.SipStatus>(
                service, METHODID_SIP_GET_SIP_STATUS)))
        .addMethod(
          getSipGetSipStatusHistoryMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              ondewo.sip.SipOuterClass.SipStatusHistoryResponse>(
                service, METHODID_SIP_GET_SIP_STATUS_HISTORY)))
        .addMethod(
          getSipPlayWavFilesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              ondewo.sip.SipOuterClass.SipPlayWavFilesRequest,
              ondewo.sip.SipOuterClass.SipStatus>(
                service, METHODID_SIP_PLAY_WAV_FILES)))
        .addMethod(
          getSipMuteMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              ondewo.sip.SipOuterClass.SipStatus>(
                service, METHODID_SIP_MUTE)))
        .addMethod(
          getSipUnMuteMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              ondewo.sip.SipOuterClass.SipStatus>(
                service, METHODID_SIP_UN_MUTE)))
        .build();
  }

  private static abstract class SipBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    SipBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return ondewo.sip.SipOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Sip");
    }
  }

  private static final class SipFileDescriptorSupplier
      extends SipBaseDescriptorSupplier {
    SipFileDescriptorSupplier() {}
  }

  private static final class SipMethodDescriptorSupplier
      extends SipBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    SipMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (SipGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new SipFileDescriptorSupplier())
              .addMethod(getSipStartSessionMethod())
              .addMethod(getSipEndSessionMethod())
              .addMethod(getSipStartCallMethod())
              .addMethod(getSipEndCallMethod())
              .addMethod(getSipTransferCallMethod())
              .addMethod(getSipRegisterAccountMethod())
              .addMethod(getSipGetSipStatusMethod())
              .addMethod(getSipGetSipStatusHistoryMethod())
              .addMethod(getSipPlayWavFilesMethod())
              .addMethod(getSipMuteMethod())
              .addMethod(getSipUnMuteMethod())
              .build();
        }
      }
    }
    return result;
  }
}
