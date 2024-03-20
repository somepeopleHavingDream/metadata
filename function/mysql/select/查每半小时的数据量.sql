SELECT
    CONCAT(
        DATE_FORMAT(create_time, '%Y-%m-%d %H'),
        IF(MINUTE(create_time) < 30, ':00', ':30')
    ) AS half_hour,
    COUNT(*) AS record_count
FROM
    user_gold_record_3
where
    create_time between '2024-03-19 00:00:00'
    and '2024-03-19 23:59:59'
GROUP BY
    CONCAT(
        DATE_FORMAT(create_time, '%Y-%m-%d %H'),
        IF(MINUTE(create_time) < 30, ':00', ':30')
    )
ORDER BY
    half_hour desc;