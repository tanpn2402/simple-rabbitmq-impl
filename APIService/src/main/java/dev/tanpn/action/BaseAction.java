package dev.tanpn.action;

import java.util.Map;

public interface BaseAction<T, V> {
    Map<String, T> doExecute(Map<String, V> pMessage);
}