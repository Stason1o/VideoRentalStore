package com.rentalstore.integration;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRentFilm() throws Exception {
        mockMvc.perform(
                        post("/api/rent")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                             [
                                                {
                                                    "name": "The Green Mile",
                                                    "amountOfDays": "10"
                                                },
                                                {
                                                    "name": "Interstellar",
                                                    "amountOfDays": "5"
                                                },
                                                {
                                                    "name": "Black Panther Wakanda Forever",
                                                    "amountOfDays": "4"
                                                },
                                                {
                                                    "name": "Harry Potter 4",
                                                    "amountOfDays": "3"
                                                },
                                                {
                                                    "name": "Guardians of the Galaxy: Holiday Special",
                                                    "amountOfDays": "2"
                                                },
                                                {
                                                    "name": "One Way",
                                                    "amountOfDays": "3"
                                                }
                                            ]
                                                                                
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceForRent").value(770))
                .andExpect(jsonPath("$.films").value(
                        Matchers.containsInAnyOrder(
                                Matchers.is("The Green Mile"),
                                Matchers.is("Interstellar"),
                                Matchers.is("Black Panther Wakanda Forever"),
                                Matchers.is("Harry Potter 4"),
                                Matchers.is("Guardians of the Galaxy: Holiday Special"),
                                Matchers.is("One Way")
                        )));
    }

    @Test
    void testReturnFilmOld() throws Exception {
        // 2 * 30 + 3 * 40 + 1 * 40
        var returnPrice = 220;

        mockMvc.perform(
                        post("/api/rent/return/old")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                             [
                                                 {
                                                     "name": "The Green Mile",
                                                     "exceededDaysOfUse": "2"
                                                 },
                                                 {
                                                     "name": "Interstellar",
                                                     "exceededDaysOfUse": "3"
                                                 },
                                                 {
                                                     "name": "Harry Potter 4",
                                                     "exceededDaysOfUse": "0"
                                                 },
                                                 {
                                                     "name": "Guardians of the Galaxy: Holiday Special",
                                                     "exceededDaysOfUse": "0"
                                                 },
                                                 {
                                                     "name": "One Way",
                                                     "exceededDaysOfUse": "1"
                                                 }
                                             ]
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(returnPrice)));
    }

    @Test
    void testReturnFilm() throws Exception {
        // 3 * 30 + 2 * 60
        var returnPrice = 150;

        var result = mockMvc.perform(
                        post("/api/rent")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                             [
                                                  {
                                                      "name": "Hobbit 1",
                                                      "amountOfDays": "3"
                                                  },
                                                  {
                                                      "name": "Avatar: The Last Airbender",
                                                      "amountOfDays": "2"
                                                  }
                                              ]
                                        """)
                )
                .andExpect(status().isOk())
                .andReturn();

        var content = new JSONObject(result.getResponse().getContentAsString());
        Integer orderId = (Integer) content.get("orderId");

        mockMvc.perform(
                        post("/api/rent/return")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("""
                                             {
                                                "orderId" :%d,
                                                "films" : [
                                                     {
                                                    "name": "Hobbit 1",
                                                    "exceededDaysOfUse": "3"
                                                },
                                                {
                                                    "name": "Avatar: The Last Airbender",
                                                    "exceededDaysOfUse": "2"
                                                }
                                                 ]
                                             }
                                        """, orderId))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(returnPrice)));
    }

    @Test
    void testReturnFilmBadRequestWhenNotFoundOrderId() throws Exception {

        mockMvc.perform(
                        post("/api/rent/return")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                             {
                                                "orderId" : 1,
                                                "films" : [
                                                     {
                                                    "name": "Hobbit 1",
                                                    "exceededDaysOfUse": "3"
                                                },
                                                {
                                                    "name": "Avatar: The Last Airbender",
                                                    "exceededDaysOfUse": "2"
                                                }
                                                 ]
                                             }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(
                        content()
                                .json("""
                                        {
                                            "message": "Order with id: '1' does not exist",
                                            "code": 404
                                        }
                                        """)
                );
    }

    @Test
    void testReturnFilmBadRequestWhenMissingSeveralFilmsFromOrder() throws Exception {
        var result = mockMvc.perform(
                        post("/api/rent")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                             [
                                                {
                                                    "name": "The Green Mile",
                                                    "amountOfDays": "10"
                                                },
                                                {
                                                    "name": "Interstellar",
                                                    "amountOfDays": "5"
                                                },
                                                {
                                                    "name": "Black Panther Wakanda Forever",
                                                    "amountOfDays": "4"
                                                },
                                                {
                                                    "name": "Harry Potter 4",
                                                    "amountOfDays": "3"
                                                },
                                                {
                                                    "name": "Guardians of the Galaxy: Holiday Special",
                                                    "amountOfDays": "2"
                                                },
                                                {
                                                    "name": "One Way",
                                                    "amountOfDays": "3"
                                                }
                                            ]
                                                                                
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceForRent").value(770))
                .andExpect(jsonPath("$.films").value(
                        Matchers.containsInAnyOrder(
                                Matchers.is("The Green Mile"),
                                Matchers.is("Interstellar"),
                                Matchers.is("Black Panther Wakanda Forever"),
                                Matchers.is("Harry Potter 4"),
                                Matchers.is("Guardians of the Galaxy: Holiday Special"),
                                Matchers.is("One Way")
                        )))
                .andReturn();

        var content = new JSONObject(result.getResponse().getContentAsString());
        Integer orderId = (Integer) content.get("orderId");

        mockMvc.perform(
                        post("/api/rent/return")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(String.format("""
                                             {
                                                "orderId" : %d,
                                                "films" : [
                                                     {
                                                    "name": "Hobbit 1",
                                                    "exceededDaysOfUse": "3"
                                                },
                                                {
                                                    "name": "Avatar: The Last Airbender",
                                                    "exceededDaysOfUse": "2"
                                                }
                                                 ]
                                             }
                                        """, orderId))
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        content()
                                .json(String.format("""
                                        {
                                            "message": "Order with id: '%d' is not complete",
                                            "code": 400
                                        }
                                        """, orderId)
                                )
                );
    }

    @Test
    void testReturnBadRequestWhenAmountOfDaysAreNegative() throws Exception {
        var result = mockMvc.perform(
                        post("/api/rent")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                             [
                                                {
                                                    "name": "The Green Mile",
                                                    "amountOfDays": "-1"
                                                },
                                                {
                                                    "name": "Interstellar",
                                                    "amountOfDays": "5"
                                                },
                                                {
                                                    "name": "Black Panther Wakanda Forever",
                                                    "amountOfDays": "4"
                                                },
                                                {
                                                    "name": "Harry Potter 4",
                                                    "amountOfDays": "3"
                                                },
                                                {
                                                    "name": "Guardians of the Galaxy: Holiday Special",
                                                    "amountOfDays": "2"
                                                },
                                                {
                                                    "name": "One Way",
                                                    "amountOfDays": "3"
                                                }
                                            ]
                                                                                
                                        """)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .json("""
                                {
                                    "message": "Requested days of use cannot be negative",
                                    "code": 400
                                }
                                """));
    }
}
