package com.internship.tmontica_admin.menu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
        log.info("now : {}" , now);
        log.info("time zone : {}", Calendar.getInstance().getTimeZone());
        Predicate<Menu> con1 = menu -> menu.getStartDate() == null && menu.getEndDate() == null;
        Predicate<Menu> con2 = menu -> menu.getStartDate().before(now) && menu.getEndDate().after(now);

        filteredMenus = allMenus.stream().filter(Menu::isUsable)
                                         .filter(con1.or(con2)).collect(Collectors.toList());

        for(Menu menu : filteredMenus){
            log.info(menu.toString());
        }

        usableMenus = filteredMenus;   // TODO : usableMenu --> scheduler에 , stream 적용
        log.info("[scheduler] end scheduler , usableMenus size : {}", usableMenus.size());
    }

    public static List<Menu> getUsableMenus(){
        return usableMenus;
    }

}
