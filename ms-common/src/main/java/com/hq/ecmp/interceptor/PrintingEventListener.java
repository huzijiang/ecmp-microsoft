package com.hq.ecmp.interceptor;

import com.alibaba.druid.support.json.JSONUtils;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xueyong
 */
public final class PrintingEventListener extends EventListener {

    private static final Logger logger = LoggerFactory.getLogger(PrintingEventListener.class);

    public static final Factory FACTORY = new Factory() {
        final AtomicLong nextCallId = new AtomicLong(1L);

        @Override
        public EventListener create(Call call) {
            long callId = nextCallId.getAndIncrement();
            return new PrintingEventListener(callId, System.nanoTime());
        }
    };

    final long callId;
    final long callStartNanos;

    PrintingEventListener(long callId, long callStartNanos) {
        this.callId = callId;
        this.callStartNanos = callStartNanos;
    }

    private void printEvent(String name, Call call) {
        long nowNanos = System.nanoTime();
        long elapsedNanos = nowNanos - callStartNanos;
        RequestBody  requestBody = call.request().body();
        logger.info("name:{} cost:{} callId:{} url:{} param:{}", name, elapsedNanos / 1000000000d, callId, call.request().url(), JSONUtils.toJSONString(requestBody==null?"":requestBody.toString()));
    }

    @Override
    public void proxySelectStart(Call call, HttpUrl url) {
        printEvent("proxySelectStart", call);
    }

    @Override
    public void proxySelectEnd(Call call, HttpUrl url, List<Proxy> proxies) {
        printEvent("proxySelectEnd", call);
    }

    @Override
    public void callStart(Call call) {
        printEvent("callStart", call);
    }

    @Override
    public void dnsStart(Call call, String domainName) {
        printEvent("dnsStart", call);
    }

    @Override
    public void dnsEnd(Call call, String domainName, List<InetAddress> inetAddressList) {
        printEvent("dnsEnd", call);
    }

    @Override
    public void connectStart(
            Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        printEvent("connectStart", call);
    }

    @Override
    public void secureConnectStart(Call call) {
        printEvent("secureConnectStart", call);
    }

    @Override
    public void secureConnectEnd(Call call, Handshake handshake) {
        printEvent("secureConnectEnd", call);
    }

    @Override
    public void connectEnd(
            Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        printEvent("connectEnd", call);
    }

    @Override
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy,
                              Protocol protocol, IOException ioe) {
        printEvent("connectFailed", call);
    }

    @Override
    public void connectionAcquired(Call call, Connection connection) {
        printEvent("connectionAcquired", call);
    }

    @Override
    public void connectionReleased(Call call, Connection connection) {
        printEvent("connectionReleased", call);
    }

    @Override
    public void requestHeadersStart(Call call) {
        printEvent("requestHeadersStart", call);
    }

    @Override
    public void requestHeadersEnd(Call call, Request request) {
        printEvent("requestHeadersEnd", call);
    }

    @Override
    public void requestBodyStart(Call call) {
        printEvent("requestBodyStart", call);
    }

    @Override
    public void requestBodyEnd(Call call, long byteCount) {
        printEvent("requestBodyEnd", call);
    }

    @Override
    public void requestFailed(Call call, IOException ioe) {
        printEvent("requestFailed", call);
    }

    @Override
    public void responseHeadersStart(Call call) {
        printEvent("responseHeadersStart", call);
    }

    @Override
    public void responseHeadersEnd(Call call, Response response) {
        printEvent("responseHeadersEnd", call);
    }

    @Override
    public void responseBodyStart(Call call) {
        printEvent("responseBodyStart", call);
    }

    @Override
    public void responseBodyEnd(Call call, long byteCount) {
        printEvent("responseBodyEnd", call);
    }

    @Override
    public void responseFailed(Call call, IOException ioe) {
        printEvent("responseFailed", call);
    }

    @Override
    public void callEnd(Call call) {
        printEvent("callEnd", call);
    }

    @Override
    public void callFailed(Call call, IOException ioe) {
        printEvent("callFailed", call);
    }
}
