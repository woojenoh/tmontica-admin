package com.internship.tmontica_admin.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.internship.tmontica_admin.menu.model.request.MenuRequest;
import com.internship.tmontica_admin.menu.validaton.MenuUpdateValidator;
import com.internship.tmontica_admin.menu.validaton.MenuValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@WebMvcTest(MenuController.class)
@Import({TestConfiguration.class, MenuValidator.class, MenuUpdateValidator.class})
public class MenuControllerTest {

    @MockBean
    private MenuService menuService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MenuValidator menuValidator;

    @Autowired
    private MenuUpdateValidator menuUpdateValidator;

    private MockMvc mockMvc;

    @Test
    public void 메뉴_추가(){

        List<Integer> optionIds = new ArrayList<>();
        optionIds.add(1);
        optionIds.add(2);

        MockMultipartFile mockMultipartFile =
                new MockMultipartFile("Condensed milk latte","Condensed milk latte-image.file","image/png",new byte[]{1,2,3,4,5,66,7,7,8,9,77,8,9,0});

        MenuRequest menuForm = MenuRequest.builder().categoryEng("Coffee").categoryKo("커피").description("달콤한 연유라떼").discountRate(10)
                                          .imgFile(mockMultipartFile).monthlyMenu(false).nameEng("Condensed milk latte").nameKo("연유라떼").optionIds(optionIds)
                                          .productPrice(2000).sellPrice(1800).stock(100).usable(true).build();

        Menu menu = modelMapper.map(menuForm, Menu.class);


        // when
        Mockito.when(menuService.addMenu(menu, optionIds, mockMultipartFile)).thenReturn(10);

        // then
//        mockMvc.perform(post("/api/menus")
//                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                       // .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        )


    }

}
