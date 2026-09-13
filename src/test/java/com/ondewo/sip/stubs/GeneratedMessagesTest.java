package com.ondewo.sip.stubs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.protobuf.Timestamp;
import java.util.stream.Stream;
import ondewo.sip.SipOuterClass;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Exercises the committed protoc output. These are the tests that catch a broken generator:
 * they build a message, push it through the real binary marshaller and read it back.
 *
 * <p>ondewo-sip-api is a single proto that does NOT set {@code java_multiple_files}, so every
 * message is nested in the outer class {@code ondewo.sip.SipOuterClass} and no
 * {@code com.ondewo.sip} classes are generated at all - the pilot's separate multi-file case
 * has no counterpart here. It also declares no {@code optional} scalar, so there is no
 * explicit-presence case to assert either; the only presence accessor in the whole client is
 * {@code SipStatus.hasTimestamp()}, which a message-typed field has in plain proto3 anyway and
 * which is covered by the round trip below.
 */
class GeneratedMessagesTest {

    @Test
    void roundTripsAnOuterClassMessage() throws Exception {
        final SipOuterClass.SipStatus original =
                SipOuterClass.SipStatus.newBuilder()
                        .setAccountName("sip-user-1@mydomain.com")
                        .setTimestamp(Timestamp.newBuilder().setSeconds(1_700_000_000L).build())
                        .setStatusType(SipOuterClass.SipStatus.StatusType.OUTGOING_CALL_CONNECTED)
                        .setCalleeId("sip-user-2@mydomain.com")
                        .putHeaders("X-Ondewo-Call-Id", "6a1b2c3d-0000-4000-8000-000000000000")
                        .setDescription("connected")
                        .build();

        final byte[] wire = original.toByteArray();
        final SipOuterClass.SipStatus parsed = SipOuterClass.SipStatus.parseFrom(wire);

        assertEquals(original, parsed);
        assertEquals("sip-user-1@mydomain.com", parsed.getAccountName());
        assertTrue(parsed.hasTimestamp());
        assertEquals(1_700_000_000L, parsed.getTimestamp().getSeconds());
        assertEquals(
                SipOuterClass.SipStatus.StatusType.OUTGOING_CALL_CONNECTED, parsed.getStatusType());
        assertEquals(
                "6a1b2c3d-0000-4000-8000-000000000000",
                parsed.getHeadersOrThrow("X-Ondewo-Call-Id"));
        assertTrue(wire.length > 0);
    }

    @Test
    void roundTripsARepeatedMessageField() throws Exception {
        final SipOuterClass.SipStatusHistoryResponse original =
                SipOuterClass.SipStatusHistoryResponse.newBuilder()
                        .addStatusHistory(
                                SipOuterClass.SipStatus.newBuilder()
                                        .setStatusType(
                                                SipOuterClass.SipStatus.StatusType.SESSION_STARTED)
                                        .build())
                        .addStatusHistory(
                                SipOuterClass.SipStatus.newBuilder()
                                        .setStatusType(
                                                SipOuterClass.SipStatus.StatusType.SESSION_ENDED)
                                        .build())
                        .build();

        final SipOuterClass.SipStatusHistoryResponse parsed =
                SipOuterClass.SipStatusHistoryResponse.parseFrom(original.toByteArray());

        assertEquals(original, parsed);
        assertEquals(2, parsed.getStatusHistoryCount());
        assertEquals(
                SipOuterClass.SipStatus.StatusType.SESSION_ENDED,
                parsed.getStatusHistory(1).getStatusType());
    }

    @Test
    void keepsTheProtoPackageInTheDescriptor() {
        // The java_package of these protos is rewritten by the compiler image, but the PROTO
        // package - what goes on the wire - must stay ondewo.sip.
        assertEquals("ondewo.sip.SipStatus", SipOuterClass.SipStatus.getDescriptor().getFullName());
        assertEquals(
                "ondewo.sip.SipStartSessionRequest",
                SipOuterClass.SipStartSessionRequest.getDescriptor().getFullName());
    }

    @ParameterizedTest(name = "{0} has the zero value {1}")
    @MethodSource("zeroValues")
    void everyEnumDeclaresItsZeroValue(final String name, final int number, final Object zeroValue) {
        assertEquals(0, number, name);
        assertEquals(name, zeroValue.toString());
    }

    private static Stream<Arguments> zeroValues() {
        // sip.proto declares exactly one enum, nested in SipStatus.
        return Stream.of(
                Arguments.of(
                        "NO_SESSION",
                        SipOuterClass.SipStatus.StatusType.NO_SESSION.getNumber(),
                        SipOuterClass.SipStatus.StatusType.forNumber(0)));
    }

    @Test
    void defaultInstancesAreEmpty() {
        final SipOuterClass.SipStatus status = SipOuterClass.SipStatus.getDefaultInstance();

        assertEquals("", status.getAccountName());
        assertFalse(status.hasTimestamp());
        assertEquals(SipOuterClass.SipStatus.StatusType.NO_SESSION, status.getStatusType());
        assertEquals(0, status.getHeadersCount());
        assertEquals(0, status.getSerializedSize());
    }
}
