package com.service.interfaces;

import com.dto.activity.RecentActivityDTO;

import java.util.List;

public interface DashboardService {

    List<RecentActivityDTO> getRecentActivityLog();

}
