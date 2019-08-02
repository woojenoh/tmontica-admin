package com.internship.tmontica_admin.banner;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BannerDao {
    //    private int id;
    //    private String usePage;
    //    private boolean usable;
    //    private String img;
    //    private String link;
    //    private Date start_date;
    //    private Date end_date;
    //    private String creatorId;
    //    private int number;

    @Insert("INSERT INTO banners(use_page, usable, img_url, link, start_date, end_date, creator_id, number)" +
            " VALUES(#{usePage}, #{usable}, #{imgUrl}, #{link}, #{startDate}, #{endDate}, #{creatorId}, #{number})")
    @Options(useGeneratedKeys=true, keyProperty="id", keyColumn = "id")
    int addBanner(Banner banner);

    @Select("SELECT * FROM banners WHERE id = #{id}")
    Banner getBannerById(int id);

    @Select("SELECT * FROM banners WHERE use_page = #{usePage} AND usable = 1")
    List<Banner> getBannersByUsePage(String usePage);

    @Select("SELECT * FROM banners WHERE use_page = #{usePage}, number = #{number}")
    Banner getBannerByUsePageAndNumber(String usePage, int number);

    @Select("SELECT * FROM banners")
    List<Banner> getAllBanner();

    @Update("UPDATE banners SET usable = 0 WHERE number = #{number} AND use_page = #{usePage} AND id != #{id}")
    void updateBannerUnusable(int number, String usePage, int id);

    @Update("UPDATE banners SET use_page = #{usePage}, img = #{img}, link = #{link}, usable = #{usable}," +
            "start_date = #{startDate}, end_Date = #{endDate}, number = #{number} " +
            "WHERE id = #{id}")
    void updateBanner(int id);

    @Delete("DELETE FROM banners WHERE id = #{id}")
    void deleteBanner(int id);

}
