package com.barbershop.service.interfaces;

import com.barbershop.dto.dashboard.RecentActivityDTO;

import java.util.List;

public interface DashboardService {

    List<RecentActivityDTO> getRecentActivityLog();

    List<RecentActivityDTO> getLowStockProductsLog();
}
