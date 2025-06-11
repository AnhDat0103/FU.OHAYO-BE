package vn.fu_ohayo.config;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PendingApprovalStorage {
    private final Map<String, String> pendingRequests = new ConcurrentHashMap<>();
    public void save(String clientId, String parentCode) {
        pendingRequests.put(clientId, parentCode);
    }
    public void remove(String clientId) {
        pendingRequests.remove(clientId);
    }
    public boolean has(String clientId) {
        return pendingRequests.containsKey(clientId);
    }
}
