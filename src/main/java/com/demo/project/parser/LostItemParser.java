package com.demo.project.parser;

import java.util.List;

import com.demo.project.model.LostItem;

public interface LostItemParser<T> {

  List<LostItem> parse(T input);

}
