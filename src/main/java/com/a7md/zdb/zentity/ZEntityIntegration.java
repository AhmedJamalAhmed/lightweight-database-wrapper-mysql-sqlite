package com.a7md.zdb.zentity;

import com.a7md.zdb.RowTypes.types.ZSqlRow;
import com.a7md.zdb.ZTable;

public abstract class ZEntityIntegration<Element extends ZSqlRow, Table extends ZTable<Element>,
        Grade extends ZSqlRow, ClassificationTable extends ZTable<Grade>,
        EntryItem extends ZSqlRow,
        EntriesItemsTable extends ZTable<EntryItem>,
        Entry extends ZSqlRow, EntriesTable extends ZTable<Entry>> {

    final private Table table;
    final private ClassificationTable classificationTable;
    final private EntriesItemsTable entriesItemsTable;
    final private EntriesTable entriesTable;

    protected ZEntityIntegration(Table table, ClassificationTable classificationTable, EntriesItemsTable entriesItemsTable, EntriesTable entriesTable) {
        this.table = table;
        this.classificationTable = classificationTable;
        this.entriesItemsTable = entriesItemsTable;
        this.entriesTable = entriesTable;
    }
}
