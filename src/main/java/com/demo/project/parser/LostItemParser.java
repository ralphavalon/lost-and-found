package com.demo.project.parser;

import java.io.InputStream;
import java.util.List;

import com.demo.project.model.LostItem;

public interface LostItemParser {

  List<LostItem> parse(InputStream input);

}
