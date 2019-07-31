package com.internship.tmontica_admin.menu;

import com.internship.tmontica_admin.option.Option;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MenuDao {

    @Insert("INSERT INTO menus(name_ko, name_eng, product_price, category_ko, category_eng, monthly_menu, usable," +
                                "img_url, description, sell_price, discount_rate, created_date, creator_id, stock, start_date, end_date)"+
            "VALUES (#{nameKo},#{nameEng}, #{productPrice}, #{categoryKo}, #{categoryEng}, #{monthlyMenu} , #{usable} ,"+
                    "#{imgUrl}, #{description}, #{sellPrice}, #{discountRate}, #{createdDate},  #{creatorId}, #{stock}, #{startDate}, #{endDate})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn = "id")   // 아이디 리턴..
    int addMenu(Menu menu);

    @Insert("INSERT INTO menu_options(menu_id, option_id) VALUES (#{menuId}, #{optionId})")
    int addMenuOption(@Param("menuId") int menuId, @Param("optionId") int optionId);

    @Select("SELECT * FROM menus WHERE id = #{id}")
    Menu getMenuById(int id);

    @Select("SELECT * FROM menus")
    List<Menu> getAllMenus();

    @Select("SELECT * FROM menus LIMIT #{limit} OFFSET #{offset}")
    List<Menu> getAllMenusByPage(int limit, int offset);

    @Select("SELECT * FROM menus WHERE category_eng = #{category} AND usable = 1 LIMIT #{limit} OFFSET #{offset}")
    List<Menu> getMenusByCategory(String category, int limit, int offset);

    @Select("SELECT * FROM menus WHERE category_eng = #{category} AND usable = 1 AND stock > 0 LIMIT #{limit} OFFSET #{offset}")
    List<Menu> getMenusByCategoryAndStock(String category, int limit, int offset);

    @Select("SELECT * FROM menus WHERE monthly_menu = 1 AND usable = 1  AND stock > 0 order by created_date desc Limit 4")
    List<Menu> getMonthlyMenus();

    @Update("UPDATE menus " +
            "SET name_ko = #{nameKo}, name_eng = #{nameEng}, product_price = #{productPrice}, " +
            "category_ko = #{categoryKo}, category_eng = #{categoryEng}, monthly_menu = #{monthlyMenu}, usable = #{usable}, " +
            "img_url = #{imgUrl}, description = #{description}, sell_price = #{sellPrice}," +
            "discount_rate = #{discountRate}, stock = #{stock}, updated_date = #{updatedDate}," +
            "updater_id = #{updaterId} WHERE id = #{id}")
    void updateMenu(Menu menu);

    @Update("UPDATE menus SET stock = #{stock} WHERE id = #{id}")
    void updateMenuStock(int id, int stock);

    @Delete("DELETE FROM menus WHERE id = #{id}")
    void deleteMenu(int id);

    @Select("SELECT * FROM menus WHERE DATE(start_date) = CURDATE() AND usable = 0")
    List<Menu> getPeriodBeforeMenu();

    @Select("SELECT * FROM menus WHERE DATE(end_date) = CURDATE() AND usable = 1")
    List<Menu> getPeriodAfterMenu();


    @Update("UPDATE menus SET usable = #{usable} WHERE id = #{id}")
    void updateMenuUsable(int id, boolean usable);


    @Select("SELECT * FROM options INNER JOIN menu_options " +
            "ON menu_options.menu_id = #{id} WHERE menu_options.option_id = options.id")
    List<Option> getOptionsById(int id);


}
