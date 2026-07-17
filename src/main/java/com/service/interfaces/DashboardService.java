package com.service.interfaces;

import com.dto.dashboard.RecentActivityDTO;

import java.util.List;

public interface DashboardService {

    List<RecentActivityDTO> getRecentActivityLog();

    List<RecentActivityDTO> getLowStockProductsLog();
}
