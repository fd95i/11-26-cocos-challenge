package com.cocos.assets.api.infrastructure.data.db.sql;

public class NeonDBConstants {
    public static final String INSTRUMENTS_QUERY = """
        SELECT o.userid,
               o.instrumentid,
               i.ticker,
               i.name,
               i.type AS instrumenttype,
               SUM(CASE o.side
                     WHEN 'BUY'      THEN o.size
                     WHEN 'CASH_IN'  THEN o.size
                     WHEN 'SELL'     THEN -o.size
                     WHEN 'CASH_OUT' THEN -o.size
                   END) AS totalsize,
               SUM(CASE o.side
                     WHEN 'BUY'      THEN o.size
                     WHEN 'CASH_IN'  THEN o.size
                     WHEN 'SELL'     THEN -o.size
                     WHEN 'CASH_OUT' THEN -o.size
                   END) * m.close AS totalvalue,
               SUM(CASE WHEN o.side = 'BUY' THEN o.size * o.price END)
                   / NULLIF(SUM(CASE WHEN o.side = 'BUY' THEN o.size END), 0) AS avgprice,
               (m.close - m.previousclose) / m.close * 100 AS dailyReturnRate,
               m.close
        FROM orders o
        JOIN instruments i ON i.id = o.instrumentid
        LEFT JOIN marketdata m
               ON m.instrumentid = i.id
              AND m.date = (SELECT MAX(m2.date)
                                FROM marketdata m2
                                WHERE m2.instrumentid = i.id)
        WHERE o.userid = ?
          AND o.status = 'FILLED'
          AND i.type <> 'MONEDA'
        GROUP BY o.userid, o.instrumentid, i.ticker, i.name, i.type, m.close, m.previousclose;
        """;

    public static final String BALANCE_QUERY = """
            SELECT SUM(CASE side
                           WHEN 'CASH_IN'  THEN size
                           WHEN 'CASH_OUT' THEN -size
                           WHEN 'SELL'     THEN size * price
                           WHEN 'BUY'      THEN -size * price
                END)
            FROM orders
            WHERE userid = ?
              AND status = 'FILLED';
            """;

    public static final String INSTRUMENTS_SEARCH_QUERY = """
            WITH totales AS (
                SELECT count(*) AS count
                FROM instruments
                WHERE LOWER(name) LIKE LOWER(:name)
                  AND LOWER(ticker) LIKE LOWER(:ticker)
                )
            SELECT i.*, totales.count
            FROM instruments i, totales
            WHERE LOWER(name) LIKE LOWER(:name)
              AND LOWER(ticker) like LOWER(:ticker)
            ORDER BY name
            LIMIT :limit OFFSET :offset;
            """;

    public static final String INSERT_ORDER_QUERY = """
            INSERT INTO "orders" (instrumentid,userid,size,price,side,status,type,datetime) VALUES (:instrumentid,:userid,:size,:price,:side,:status,:type,:datetime);
            """;

    public static final String GET_CLOSE_QUERY = """
            SELECT close
            FROM marketdata m
            WHERE instrumentid = ?
              AND date = (SELECT MAX(m2.date) FROM marketdata m2 WHERE m2.instrumentid = m.instrumentid)
            """;

    public static final String GET_TOTAL_SIZE_FOR_INSTRUMENT_BY_USER_QUERY = """
            SELECT SUM(CASE side
                     WHEN 'BUY'      THEN size
                     WHEN 'CASH_IN'  THEN size
                     WHEN 'SELL'     THEN -size
                     WHEN 'CASH_OUT' THEN -size
                   END) AS totalsize
            FROM orders
            WHERE userid = ? and instrumentid = ?
              AND status = 'FILLED';
            """;
}