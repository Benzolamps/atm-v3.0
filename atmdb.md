#### 1) 建立```atmdb```数据库
```sql
CREATE DATABASE atmdb DEFAULT CHARACTER SET utf8 COLLATE utf8_bin;
```

#### 2) 建立```atmdb_user```表 (用户表)
```sql
CREATE TABLE atmdb_user (
    user_id CHAR(50) PRIMARY KEY COMMENT '主键', 
    user_card_id CHAR(16) NOT NULL UNIQUE COMMENT '用户银行卡号',
    user_balance DOUBLE NOT NULL COMMENT '用户余额',
    user_password CHAR(6) NOT NULL COMMENT '用户密码',
    user_status TINYINT NOT NULL 
	    COMMENT '账号状态, 1-正常, 2-已锁定, 3-已销户',
    user_name VARCHAR(20) NOT NULL COMMENT '用户姓名',
    user_id_no CHAR(18) NOT NULL COMMENT '用户身份证号',
    user_gender TINYINT NOT NULL COMMENT '用户性别, 1-男, 2-女',
    user_birth DATE NOT NULL COMMENT '用户出生日期, yyyy-MM-dd',
    user_address VARCHAR(50) COMMENT '用户家庭住址',
    user_remark VARCHAR(200) COMMENT '用户备注'
) ENGINE INNODB;
```

#### 3) 建立```atmdb_record```表 (交易记录表)
```sql
CREATE TABLE atmdb_record (
    record_id CHAR(50) PRIMARY KEY COMMENT '主键',
    record_serial_no CHAR(18) NOT NULL COMMENT '交易流水号',
    record_time TIMESTAMP NOT NULL COMMENT '交易时间',
    record_type TINYINT NOT NULL COMMENT '交易类型, 1-取款, 2-存款, 3-转账',
    record_source CHAR(50) NOT NULL COMMENT '支出账户',
    record_dest CHAR(50) NOT NULL COMMENT '收入账户',
    record_amount DOUBLE NOT NULL COMMENT '交易金额',
    record_balance_source DOUBLE COMMENT '交易后支出账户余额',
    record_balance_dest DOUBLE COMMENT '交易后收入账户余额',
    record_result TINYINT NOT NULL COMMENT '交易结果, 1-成功, 2-失败'
) ENGINE INNODB;
```

#### 4) 建立```view_user```视图 (用户视图)
```sql
-- 查询用户信息, 并将数字替换为汉字
CREATE VIEW view_user AS

SELECT
 
-- 银行卡号
u.user_card_id AS user_card_id, 

-- 余额
u.user_balance AS user_balance, 

-- 密码
u.user_password AS user_password, 

-- 用户姓名
u.user_name AS user_name, 

-- 用户身份证号
u.user_id_no AS user_id_no,


-- 用户性别
IF(u.user_gender = 1, '男', '女') AS user_gender, 

-- 用户家庭住址
u.user_address AS user_address, 

-- 用户出生日期
u.user_birth AS user_birth, 

-- 用户状态
CASE u.user_status 
    WHEN 1 THEN '正常'
    WHEN 2 THEN '已锁定'
    ELSE '已销户'
END AS user_status, 

-- 用户备注
u.user_remark AS user_remark

FROM atmdb_user AS u

-- id以$$$开头的不是用户
WHERE u.user_id NOT LIKE '$$$%';   
```

#### 5) 建立```view_admin```视图 (管理员视图)
```sql
-- 查询管理员的卡号跟密码
CREATE VIEW view_admin AS

SELECT 

-- 管理员卡号
u.user_card_id AS user_card_id, 

-- 管理员密码
u.user_password AS user_password

FROM atmdb_user u

-- id以$$$Admin开头的是管理员账户
WHERE u.user_id LIKE '$$$Admin%';   
```

#### 6) 建立```view_record```视图 (用户交易记录视图)
```sql
CREATE VIEW view_record AS

-- 银行卡号 流水号 交易时间 交易类型 交易对象 交易对象姓名 交易金额 交易后余额
SELECT 

-- 银行卡号
u.user_card_id AS user_card_id,

-- 流水号
r.record_serial_no AS record_serial_no,

-- 交易时间
r.record_time AS record_time,

-- 交易类型
CASE r.record_type
    WHEN 1 THEN '取款'
    WHEN 2 THEN '存款'
    WHEN 3 THEN IF(u.user_id = r.record_source, '转账-支出', '转账-收入')
END AS record_type,

-- 交易对象银行卡号
ut.user_card_id AS record_target,

-- 交易对象姓名
ut.user_name AS record_target_name,

-- 交易金额
r.record_amount AS record_amount,

-- 交易后余额
CASE u.user_id
    WHEN r.record_source THEN r.record_balance_source
    WHEN r.record_dest THEN r.record_balance_dest
END AS record_balance,

-- 交易结果
IF(r.record_result = 1, '交易成功', '交易失败') AS record_result

FROM atmdb_user AS u 
-- 提取用户信息
INNER JOIN atmdb_record AS r
-- xor代表异或
ON r.record_source = u.user_id XOR r.record_dest = u.user_id
-- 提取交易对象的信息
INNER JOIN atmdb_user AS ut
ON ut.user_id = 
-- 找出交易对象
CASE u.user_id
    WHEN r.record_source THEN r.record_dest
    WHEN r.record_dest THEN r.record_source
END

-- 找出所有用户
WHERE u.user_id NOT LIKE '$$$%'

-- 先按银行卡号排序, 再按流水号排序
ORDER BY u.user_card_id, r.record_serial_no;
```

#### 7) 建立```trigger_user_insert``` 触发器 (插入用户)
```sql
CREATE TRIGGER trigger_user_insert 
-- before/after insert/delete/update
BEFORE INSERT ON atmdb_user 
FOR EACH ROW
BEGIN   
    -- 主键
    -- ifnull 表示前者为null, 值为后者
    SET new.user_id = IFNULL(new.user_id, UUID()); 

    -- 性别
    IF new.user_gender NOT IN (1, 2) THEN	
        -- 提示错误
        SIGNAL SQLSTATE 'HY000' 
        SET MESSAGE_TEXT = 'user_gender 字段只能取 1-男, 2-女';  
    END IF;

    -- 银行卡号
    SET new.user_card_id = IFNULL(
	    new.user_card_id, 
	    CONCAT(
            'BC180',
            new.user_gender,
            -- date_format将日期转换为指定格式字符串
            DATE_FORMAT(new.user_birth, '%Y%m%d'),
						
            -- 生成四位随机号
            -- right从右往左截取指定位数字符串
            -- floor向下取整
            -- rand生成0~1之间的随机数
            RIGHT(10000 + FLOOR(RAND() * 10000), 4)
        )
    );

    -- 用户状态
    SET new.user_status = IFNULL(new.user_status, 1);
    IF new.user_status NOT IN (1, 2, 3) THEN	
        SIGNAL SQLSTATE 'HY000' 
        SET MESSAGE_TEXT 
        = 'user_status 字段只能取 1-正常, 2-已锁定, 3-已销户';  
    END IF;
		
    -- 余额
    SET new.user_balance = IFNULL(new.user_balance, 0.0);
END;
```

#### 8) 建立```trigger_user_update```触发器 (更新用户)
```sql
CREATE TRIGGER trigger_user_update 
BEFORE UPDATE ON atmdb_user 
FOR EACH ROW
BEGIN   
    -- 主键
    SET new.user_id = old.user_id; 

    -- 银行卡号
    SET new.user_card_id = old.user_card_id;
     
    -- 性别
    IF new.user_gender NOT IN (1, 2) THEN	
        SIGNAL SQLSTATE 'HY000' 
        SET MESSAGE_TEXT = 'user_gender 字段只能取 1-男, 2-女';  
    END IF;

    -- 用户状态
    SET new.user_status = IFNULL(new.user_status, 1);
    IF new.user_status NOT IN (1, 2, 3) THEN	
        SIGNAL SQLSTATE 'HY000' 
        SET MESSAGE_TEXT 
        = 'user_status 字段只能取 1-正常, 2-已锁定, 3-已销户';  
    END IF;
		
    -- 余额
    SET new.user_balance = IFNULL(new.user_balance, 0.0);
END;
```

#### 9) 建立```trigger_record_insert```触发器 (插入交易记录)
```sql
CREATE TRIGGER trigger_record_insert
BEFORE INSERT ON atmdb_record 
FOR EACH ROW
BEGIN   
    -- 主键
    SET new.record_id = IFNULL(new.record_id, UUID());

    -- 交易时间
    SET new.record_time = NOW();

    -- 流水号
    SET new.record_serial_no = IFNULL(
        new.record_serial_no, 
        CONCAT(
            DATE_FORMAT(new.record_time, '%Y%m%d%H%i%s'),
            RIGHT(10000 + FLOOR(RAND() * 10000), 4)
        )
    );

    -- 交易类型
    SET new.record_type = IFNULL(new.record_type, 1);
    IF new.record_type NOT IN (1, 2, 3) THEN	
        SIGNAL SQLSTATE 'HY000' 
        SET MESSAGE_TEXT 
        = 'record_type 字段只能取 1-取款, 2-已锁定, 3-已销户';  
    END IF;

		-- 用于记录交易后支出账户余额
		SET @balance_source = (
        SELECT u.user_balance 
        FROM atmdb_user AS u 
        WHERE u.user_id = new.record_source
    );

		-- 用于记录交易后收入账户余额
		SET @balance_dest = (
        SELECT u.user_balance 
        FROM atmdb_user AS u 
        WHERE u.user_id = new.record_dest
    );

	-- 取款或转账时余额充足
	IF @balance_source >= new.record_amount OR new.record_type = 2 THEN
        IF (new.record_type <> 2) THEN 
            -- 支出方扣款后金额
            SET @balance_source = @balance_source - new.record_amount;
        ENDIF
        
        IF (new.record_type <> 1) THEN 
            -- 收入方到账后金额
            SET @balance_dest = @balance_dest + new.record_amount;
        END

        -- 取款或转账时更新支出方余额
        UPDATE atmdb_user 
        SET user_balance = @balance_source 
        WHERE user_id = new.record_source 
        AND new.record_type <> 2;

        -- 存款或转账时更新收入方余额
        UPDATE atmdb_user 
        SET user_balance = @balance_dest 
        WHERE user_id = new.record_dest 
        AND new.record_type <> 1;

        -- 交易成功
        SET new.record_result = 1;
	-- 取款或转账时余额不足
	ELSE 
        -- 交易失败
        SET new.record_result = 2;
	END IF;

    -- 记录交易后支出方余额
    SET new.record_balance_source = @balance_source;

    -- 记录交易后收入方余额
    SET new.record_balance_dest = @balance_dest;	
END
```

#### 10) 建立```transfer```函数 (转账)
```sql
/**
    转账
    @card_id 银行卡号
    @transfer_id 收款方银行卡号
    @amount 金额
    @returns 交易结果, 1-成功, 2-失败
 */
CREATE FUNCTION transfer (
    source_id CHAR(18),
    dest_id CHAR(18),
    amount DOUBLE
) RETURNS TINYINT
BEGIN
    -- 判断交易类型
    IF source_id = 'SYSTEM' THEN
        SET @type = 2;
    ELSEIF dest_id = 'SYSTEM' THEN
        SET @type = 1;
    ELSE
        SET @type = 3;
    END IF;

    -- 支出方是转账账户
    SET @source = (
        SELECT u.user_id
        FROM atmdb_user AS u
        WHERE u.user_card_id = source_id
    );

    -- 收入方是收账账户
    SET @dest = (
        SELECT u.user_id 
        FROM atmdb_user AS u 
        WHERE u.user_card_id = dest_id
    );

    IF @source IS NULL THEN
        -- 提示错误
	    SET @msg = CONCAT('支出方', source_id, '不存在');
        SIGNAL SQLSTATE 'HY000' 
	    SET MESSAGE_TEXT = @msg; 
    END IF;

    IF @dest IS NULL THEN
        -- 提示错误
        SET @msg = CONCAT('收入方', dest_id, '不存在');
        SIGNAL SQLSTATE 'HY000' 
        SET MESSAGE_TEXT = @msg;  
    END IF;

    IF @source = @dest THEN
        -- 提示错误
        SIGNAL SQLSTATE 'HY000' 
        SET MESSAGE_TEXT = '支出方与收入方相同'; 
    END IF;

    -- 插入交易记录
    INSERT INTO atmdb_record 
    (record_source, record_dest, record_type, record_amount)
    VALUES (@source, @dest, @type, amount);

	-- 获取交易结果
	SET @result = (
        SELECT r.record_result 
        FROM atmdb_record AS r 
        ORDER BY r.record_serial_no DESC 
        LIMIT 0, 1
	);

	RETURN @result;
END;
```

#### 10) 建立```deposit```函数 (存款)
```sql
/**
    存款
    @card_id 银行卡号
    @amount 金额
    @returns 交易结果, 1-成功, 2-失败
 */
CREATE FUNCTION deposit (
    card_id CHAR(18),
    amount DOUBLE
) RETURNS TINYINT
BEGIN
    -- 支出方是SYSTEM
    -- 收入方是存款账户
    RETURN transfer('SYSTEM', card_id, amount);
END;
```

#### 11) 建立```debit```函数 (取款)
```sql
/**
    取款
    @card_id 银行卡号
    @amount 金额
    @returns 交易结果, 1-成功, 2-失败
 */
CREATE FUNCTION debit (
    card_id CHAR(18),
    amount DOUBLE
) RETURNS TINYINT
BEGIN
    -- 支出方是取款账户
    -- 收入方是SYSTEM
    RETURN transfer(card_id, 'SYSTEM' amount);
END;
```

#### 12) 建立```add_user```函数 (开户)
```sql
/**
    开户
    @user_id_no 身份证号
    @user_name 姓名
    @user_gender 性别
    @user_birth 出生日期
    @user_address 家庭住址
    @user_password 密码
    @returns 受影响的行数
 */
CREATE FUNCTION add_user (
    user_id_no CHAR(18),
    user_name CHAR(20),
    user_gender TINYINT,
    user_birth DATE,
    user_address VARCHAR(50),
    user_password CHAR(6)
) RETURNS INTEGER
BEGIN
    INSERT INTO atmdb_user (
        user_id_no, 
        user_name, 
        user_gender, 
        user_birth, 
        user_address, 
        user_password
    )
    VALUES (
        user_id_no, 
        user_name, 
        user_gender, 
        user_birth, 
        user_address, 
        user_password
    );

    RETURN ROW_COUNT();
END
```

#### 13) 建立```delete_user```函数 (销户)
```sql
/**
    销户
    @card_id 银行卡号
    @returns 受影响的行数
 */
CREATE FUNCTION delete_user (card_id CHAR(18)) 
RETURNS INTEGER
BEGIN
    UPDATE atmdb_user 
    SET user_status = 3 
    WHERE user_card_id = card_id;

    RETURN ROW_COUNT();
END;
```

#### 13) 建立```lock_user```函数 (锁定账户)
```sql
/**
    锁定账户
    @card_id 银行卡号
    @returns 受影响的行数
 */
CREATE FUNCTION lock_user (card_id CHAR(18)) 
RETURNS INTEGER
BEGIN
    UPDATE atmdb_user 
    SET user_status = 2 
    WHERE user_card_id = card_id;

    RETURN ROW_COUNT();
END;
```

#### 14) 建立```unlock_user```函数 (解锁账户)
```sql
/**
    解锁账户
    @card_id 银行卡号
    @returns 受影响的行数
 */
CREATE FUNCTION unlock_user (card_id CHAR(18)) 
RETURNS INTEGER
BEGIN
    UPDATE atmdb_user 
    SET user_status = 1 
    WHERE user_card_id = card_id;

    RETURN ROW_COUNT();
END;
```


#### 14) 建立```reset_password```函数 (重置密码)
```sql
/**
    重置密码
    @card_id 银行卡号
    @password 新密码
    @returns 受影响的行数
 */
CREATE FUNCTION reset_password (
	card_id CHAR(18),
	password_ CHAR(6)
) 
RETURNS INTEGER
BEGIN
    UPDATE atmdb_user 
    SET user_password = password_ 
    WHERE user_card_id = card_id;

    RETURN ROW_COUNT();
END;
```


