package com.hcl.physician_portal.mapper;

import com.hcl.physician_portal.dto.ViewWorkstation;
import com.hcl.physician_portal.model.Workstation;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WorkstationMapper {
    public ViewWorkstation workstationEntityToViw(Workstation workStation) {
        ViewWorkstation viewWorkstation = new ViewWorkstation();

        viewWorkstation.setId(workStation.getId());
        viewWorkstation.setAddress(workStation.getAddress());
        viewWorkstation.setLandmark(workStation.getLandmark());
        // viewWorkstation.setPhysicianID(workStation.getPhysician().getId());
        viewWorkstation.setCreatedAt(workStation.getCreatedAt());
        viewWorkstation.setUpdatedAt(workStation.getUpdatedAt());

        return viewWorkstation;
    }

    public List<ViewWorkstation> workstationEntityListToViewList(List<Workstation> workstationList) {
        List<ViewWorkstation> viewWorkstationList = new ArrayList<>();
        for (Workstation workstation : workstationList) {
            ViewWorkstation viewWorkstation = new ViewWorkstation();

            viewWorkstation.setId(workstation.getId());
            // viewWorkstation.setPhysicianID(workstation.getPhysician().getId());
            viewWorkstation.setAddress(workstation.getAddress());
            viewWorkstation.setLandmark(workstation.getLandmark());
            viewWorkstation.setCreatedAt(workstation.getCreatedAt());
            viewWorkstation.setUpdatedAt(workstation.getUpdatedAt());

            viewWorkstationList.add(viewWorkstation);
        }
        return viewWorkstationList;
    }

}