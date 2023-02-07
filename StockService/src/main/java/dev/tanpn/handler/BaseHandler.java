package dev.tanpn.handler;

import java.util.Map;

public interface BaseHandler<T, V> {
    Map<String, T> doExecute(Map<String, V> pMessage);
}
