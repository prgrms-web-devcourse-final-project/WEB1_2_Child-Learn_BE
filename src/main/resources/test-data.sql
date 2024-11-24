-- Member 데이터 생성
INSERT INTO member (id, login_id, pw, username, email, birth, points, created_at, updated_at)
VALUES (1, 'test1', 'password1', 'TestUser1', 'test1@test.com', '1990-01-01', 1000, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- MidStockTrade 데이터 생성
INSERT INTO mid_stock_trade (id, trade_point, price_per_stock, trade_type, mid_stock_id, member_id, CREATE_DATE, MODIFIED_DATE)
VALUES (1, 1000, 77000, 'BUY', 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
