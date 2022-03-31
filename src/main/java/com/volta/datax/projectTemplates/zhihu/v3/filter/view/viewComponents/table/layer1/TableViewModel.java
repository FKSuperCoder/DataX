package com.volta.datax.projectTemplates.zhihu.v3.filter.view.viewComponents.table.layer1;

import com.volta.datax.projectTemplates.zhihu.v3.filter.view.viewComponents.table.layer0.TableHeaderItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableViewModel<T> {

    String tableId;
    ArrayList<TableHeaderItem> header;
    ArrayList<T> row;
}
