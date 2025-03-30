package ro.mpp2025.repository;
import ro.mpp2025.model.ComputerRepairRequest;
import ro.mpp2025.model.RequestStatus;

import java.util.List;

public interface ComputerRepairRequestRepository extends Repository<Integer,ComputerRepairRequest>{
    List<ComputerRepairRequest> findByOwnerName(String name);
    List<ComputerRepairRequest> findByModel(String model);
    List<ComputerRepairRequest> filterByStatus(RequestStatus status);
    List<ComputerRepairRequest> filterByModelAndStatus(String model, RequestStatus status);



}
