package com.dm.database;

/**
 *
 * @author zy
 *  the enum parser type
 */
public enum SQLType {

    /**
     * Select parser type.
     */
    SELECT(0),
    /**
     * Insert parser type.
     */
    INSERT(1),
    /**
     * Update parser type.
     */
    UPDATE(2),
    /**
     * Delete parser type.
     */
    DELETE(3),
    /**
     * Select for update parser type.
     */
    SELECT_FOR_UPDATE(4),
    /**
     * Replace parser type.
     */
    REPLACE(5),
    /**
     * Truncate parser type.
     */
    TRUNCATE(6),
    /**
     * Create parser type.
     */
    CREATE(7),
    /**
     * Drop parser type.
     */
    DROP(8),
    /**
     * Load parser type.
     */
    LOAD(9),
    /**
     * Merge parser type.
     */
    MERGE(10),
    /**
     * Show parser type.
     */
    SHOW(11),
    /**
     * Alter parser type.
     */
    ALTER(12),
    /**
     * Rename parser type.
     */
    RENAME(13),
    /**
     * Dump parser type.
     */
    DUMP(14),
    /**
     * Debug parser type.
     */
    DEBUG(15),
    /**
     * Explain parser type.
     */
    EXPLAIN(16),
    /**
     * Stored procedure
     */
    PROCEDURE(17),
    /**
     * Desc parser type.
     */
    DESC(18),
    /**
     * Select last insert id
     */
    SELECT_LAST_INSERT_ID(19),
    /**
     * Select without table parser type.
     */
    SELECT_WITHOUT_TABLE(20),
    /**
     * Create sequence parser type.
     */
    CREATE_SEQUENCE(21),
    /**
     * Show sequences parser type.
     */
    SHOW_SEQUENCES(22),
    /**
     * Get sequence parser type.
     */
    GET_SEQUENCE(23),
    /**
     * Alter sequence parser type.
     */
    ALTER_SEQUENCE(24),
    /**
     * Drop sequence parser type.
     */
    DROP_SEQUENCE(25),
    /**
     * Tddl show parser type.
     */
    TDDL_SHOW(26),
    /**
     * Set parser type.
     */
    SET(27),
    /**
     * Reload parser type.
     */
    RELOAD(28),
    /**
     * Select union parser type.
     */
    SELECT_UNION(29),
    /**
     * Create table parser type.
     */
    CREATE_TABLE(30),
    /**
     * Drop table parser type.
     */
    DROP_TABLE(31),
    /**
     * Alter table parser type.
     */
    ALTER_TABLE(32),
    /**
     * Save point parser type.
     */
    SAVE_POINT(33),
    /**
     * Select from update parser type.
     */
    SELECT_FROM_UPDATE(34),
    /**
     * multi delete/update
     */
    MULTI_DELETE(35),
    /**
     * Multi update parser type.
     */
    MULTI_UPDATE(36),
    /**
     * Create index parser type.
     */
    CREATE_INDEX(37),
    /**
     * Drop index parser type.
     */
    DROP_INDEX(38),
    /**
     * Kill parser type.
     */
    KILL(39),
    /**
     * Release dblock parser type.
     */
    RELEASE_DBLOCK(40),
    /**
     * Lock tables parser type.
     */
    LOCK_TABLES(41),
    /**
     * Unlock tables parser type.
     */
    UNLOCK_TABLES(42),
    /**
     * Check table parser type.
     */
    CHECK_TABLE(43),

    /**
     * Select found rows.
     */
    SELECT_FOUND_ROWS(44),

    /**
     * Insert ingore parser type.
     */
    INSERT_INGORE(101),
    /**
     * Insert on duplicate update parser type.
     */
    INSERT_ON_DUPLICATE_UPDATE(102);

    private int i;

    private SQLType(int i) {
        this.i = i;
    }

    /**
     * Value int.
     *
     * @return the int
     */
    public int value() {
        return this.i;
    }

    /**
     * Value of parser type.
     *
     * @param i the
     * @return the parser type
     */
    public static SQLType valueOf(int i) {
        for (SQLType t : values()) {
            if (t.value() == i) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid SQLType:" + i);
    }
}
