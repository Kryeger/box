package logic.service;

import logic.Manager;

import java.util.HashMap;

public class ManagerService {

    private static final ManagerService _instance = new ManagerService();

    private HashMap<String, Manager> _managers = new HashMap<>();

    private ManagerService() {}

    public static ManagerService get_instance() {
        return _instance;
    }

    public void createManager(String id, String firstName, String lastName, String teamId) {
        _instance._managers.put(id, new Manager(id, firstName, lastName, teamId));
    }

    public static void insertManager(Manager manager){
        _instance._managers.put(manager.getId(), manager);
    }

    public static Manager getManagerById(String id){
        if(_instance._managers.containsKey(id)){
            return _instance._managers.get(id);
        }
        return null;
    }

    public static boolean checkExistence(String managerId){
        return _instance._managers.containsKey(managerId);
    }

    public static HashMap<String, Manager> getAll() {
        return _instance._managers;
    }
 }
