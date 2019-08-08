package com.internship.tmontica_admin.menu;

import com.internship.tmontica_admin.menu.model.vo.MenuIdName;
import com.internship.tmontica_admin.option.Option;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface MenuDao {

    @Insert("INSERT INTO menus(name_ko, name_eng, product_price, category_ko, category_eng, monthly_menu, usable," +
                                "img_url, description, sell_price, discount_rate, creator_id, stock, start_date, end_date)"+
            "VALUES (#{nameKo},#{nameEng}, #{productPrice}, #{categoryKo}, #{categoryEng}, #{monthlyMenu} , #{usable} ,"+
                    "#{imgUrl}, #{description}, #{sellPrice}, #{discountRate}, #{creatorId}, #{stock}, #{startDate}, #{endDate})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn = "id")
    int addMenu(Menu menu);

    @Insert("INSERT INTO menu_options(menu_id, option_id) VALUES (#{menuId}, #{optionId})")
    int addMenuOption(@Param("menuId") int menuId, @Param("optionId") int optionId);

    @Delete("DELETE FROM menu_options WHERE menu_id = #{menuId}")
    int deleteMenuOption(@Param("menuId") int menuId );

    @Select("SELECT * FROM menus WHERE id = #{id} AND deleted = 0")
    Menu getMenuById(int id);

    @Select("SELECT * FROM menus WHERE deleted = 0 ORDER BY created_date DESC")
    List<Menu> getAllMenus();

    @Select("SELECT * FROM menus WHERE deleted = 0 ORDER BY created_date DESC LIMIT #{limit} OFFSET #{offset}")
    List<Menu> getAllMenusByPage(int limit, int offset);

    @Select("SELECT * FROM menus WHERE category_eng = #{category} AND deleted = 0 ORDER BY created_date DESC LIMIT #{limit} OFFSET #{offset}")
    List<Menu> getMenusByCategory(String category, int limit, int offset);


    @Update("UPDATE menus " +
            "SET name_ko = #{nameKo}, name_eng = #{nameEng}, product_price = #{productPrice}, " +
            "category_ko = #{categoryKo}, category_eng = #{categoryEng}, monthly_menu = #{monthlyMenu}, usable = #{usable}, " +
            "img_url = #{imgUrl}, description = #{description}, sell_price = #{sellPrice}," +
            "discount_rate = #{discountRate}, stock = #{stock}, updated_date = #{updatedDate}," +
            "start_date = #{startDate}, end_Date = #{endDate},"+
            "updater_id = #{updaterId} WHERE id = #{id}")
    int updateMenu(Menu menu);

    @Update("UPDATE menus SET stock = #{stock} WHERE id = #{id}")
    void updateMenuStock(int id, int stock);

    @Delete("UPDATE menus SET deleted = 1 WHERE id = #{id}")
    int deleteMenu(int id);


    @Update("UPDATE menus SET monthly_menu = #{monthlyMenu} WHERE id = #{id}")
    void updateMonthlyMenu(int id, boolean monthlyMenu);


    @Select("SELECT * FROM options INNER JOIN menu_options " +
            "ON menu_options.menu_id = #{id} WHERE menu_options.option_id = options.id")
    List<Option> getOptionsById(int id);

    @Select("SELECT count(*) FROM menus ")
    int getAllMenuCnt();

    @Select("SELECT count(*) FROM menus WHERE category_eng = #{category}")
    int getCategoryMenuCnt(String category);

    @Select("SELECT id, name_ko FROM menus")
    List<MenuIdName> getAllMenuIdAndName();

}
