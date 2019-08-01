package com.internship.tmontica_admin.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class MenuScheduler {
    private MenuDao menuDao;
    private static List<Menu> usableMenus;

    public MenuScheduler(MenuDao menuDao){
        this.menuDao = menuDao;
        usableMenus = new ArrayList<>();
    }

    @Scheduled(cron = "0 * * * * *")
    public void filteredMenu(){
        log.info("[scheduler] start scheduler");
        List<Menu> allMenus = menuDao.getAllMenus();
        List<Menu> filteredMenus = new ArrayList<>();
        Date now = new Date();

        for(Menu menu : allMenus){
            if(menu.getStartDate() == null && menu.getEndDate() == null) {
                if(menu.isUsable())
                    filteredMenus.add(menu);
            }else if(menu.getStartDate().before(now) && menu.getEndDate().after(now)){
                filteredMenus.add(menu);
            }
        }

//        filteredMenus = allMenus.stream().filter(menu -> menu.getStartDate() == null)
//                                         .filter(menu -> )

        usableMenus = filteredMenus;   // TODO : usableMenu --> scheduler에 , stream 적용
        log.info("[scheduler] end scheduler , usableMenus size : {}", usableMenus.size());
    }

    public static List<Menu> getUsableMenus(){
        return usableMenus;
    }

}
