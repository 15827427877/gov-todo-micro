package com.gov.systemservice.controller;

import com.gov.systemservice.pojo.Dict;
import com.gov.systemservice.service.DictService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DictControllerTest {

    @InjectMocks
    private DictController dictController;

    @Mock
    private DictService dictService;

    @Test
    public void testList() {
        List<Dict> dicts = new ArrayList<>();
        Dict dict = new Dict();
        dict.setId(1L);
        dict.setDictType("todo_status");
        dict.setDictCode("0");
        dict.setDictName("待办");
        dict.setDictValue("0");
        dicts.add(dict);

        // Mock dictService.getAllDicts() method
        // when(dictService.getAllDicts()).thenReturn(dicts);

        // ResponseEntity<Result<List<Dict>>> response = dictController.list();
        // assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetByType() {
        List<Dict> dicts = new ArrayList<>();
        Dict dict = new Dict();
        dict.setId(1L);
        dict.setDictType("todo_status");
        dict.setDictCode("0");
        dict.setDictName("待办");
        dict.setDictValue("0");
        dicts.add(dict);

        // Mock dictService.getDictsByType() method
        // when(dictService.getDictsByType("todo_status")).thenReturn(dicts);

        // ResponseEntity<Result<List<Dict>>> response = dictController.getByType("todo_status");
        // assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
