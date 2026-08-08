CREATE OR REPLACE FUNCTION calculate_order_total(p_order_id UUID)
RETURNS NUMERIC(19, 2)
LANGUAGE plpgsql
AS $$
DECLARE
    v_total NUMERIC(19, 2);
BEGIN

    SELECT COALESCE(
        SUM(unit_price * quantity),
        0
    )
    INTO v_total
    FROM order_items
    WHERE order_id = p_order_id;

    RETURN v_total;

END;
$$;