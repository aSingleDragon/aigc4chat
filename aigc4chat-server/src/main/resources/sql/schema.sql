CREATE TABLE IF NOT EXISTS wechat_user
(
    -- 用户唯一标识符
    uin                  INTEGER,
    -- 用户名
    user_name            TEXT PRIMARY KEY,
    -- 昵称
    nick_name            TEXT,
    -- 头像URL
    head_img_url         TEXT,
    -- 备注名称
    remark_name          TEXT,
    -- 拼音首字母缩写
    py_initial           TEXT,
    -- 拼音全拼
    py_quan_pin          TEXT,
    -- 备注拼音首字母缩写
    remark_py_initial    TEXT,
    -- 备注拼音全拼
    remark_py_quan_pin   TEXT,
    -- 隐藏输入栏标志位
    hide_input_bar_flag  INTEGER,
    -- 是否星标好友
    star_friend          INTEGER,
    -- 性别
    sex                  INTEGER,
    -- 个性签名
    signature            TEXT,
    -- 是否为公众号或应用账号
    app_account_flag     INTEGER,
    -- 验证标志位
    verify_flag          INTEGER,
    -- 联系人标志位
    contact_flag         INTEGER,
    -- Web微信插件开关
    web_wx_plugin_switch INTEGER,
    -- 头像标志位
    head_img_flag        INTEGER,
    -- SNS标志位
    sns_flag             INTEGER,
    -- 所属用户的uin
    owner_uin            INTEGER,
    -- 成员数量
    member_count         INTEGER,
    -- 状态
    statues              INTEGER,
    -- 属性状态
    attr_status          INTEGER,
    -- 成员状态
    member_status        INTEGER,
    -- 省份
    province             TEXT,
    -- 城市
    city                 TEXT,
    -- 别名
    alias                TEXT,
    -- 是否为统一朋友
    uni_friend           INTEGER,
    -- 展示名称
    display_name         TEXT,
    -- 群聊ID
    chat_room_id         INTEGER,
    -- 关键词
    key_word             TEXT,
    -- 是否为群主
    is_owner             INTEGER,
    -- 加密的群聊ID
    encrypt_chat_room_id TEXT,
    -- 创建时间
    created_time         DATETIME,
    -- 更新时间
    updated_time         DATETIME
);

-- 群成员
CREATE TABLE IF NOT EXISTS wechat_group_member
(
    -- 主键
    uuid            TEXT PRIMARY KEY,
    -- 群名称
    group_user_name TEXT NOT NULL,
    -- 用户名称
    user_name       TEXT NOT NULL,
    -- 昵称
    nick_name       TEXT,
    -- 名片
    display_name    TEXT,
    -- 创建时间
    created_time    DATETIME,
    -- 更新时间
    updated_time    DATETIME
);

-- 创建微信消息表
CREATE TABLE IF NOT EXISTS wechat_message
(
    -- 消息ID
    msg_id                  INTEGER PRIMARY KEY,
    -- 发送者用户名
    from_user_name          TEXT    NOT NULL,
    -- 接收者用户名
    to_user_name            TEXT    NOT NULL,
    -- 消息类型（文本、图片、语音、视频等）
    msg_type                INTEGER NOT NULL,
    -- 消息内容（文本消息时有效）
    content                 TEXT,
    -- 消息状态
    status                  INTEGER,
    -- 图片状态
    img_status              INTEGER,
    -- 消息创建时间（Unix时间戳）
    create_time             INTEGER NOT NULL,
    -- 语音消息长度（单位：秒）
    voice_length            INTEGER,
    -- 音频播放长度
    play_length             INTEGER,
    -- 文件名（附件消息时有效）
    file_name               TEXT,
    -- 文件大小（附件消息时有效）
    file_size               TEXT,
    -- 媒体资源ID（多媒体消息时有效）
    media_id                TEXT,
    -- 资源URL（多媒体消息时有效）
    url                     TEXT,
    -- 应用消息类型
    app_msg_type            INTEGER,
    -- 状态通知代码
    status_notify_code      INTEGER,
    -- 状态通知用户名
    status_notify_user_name TEXT,
    -- 是否转发消息标志
    forward_flag            INTEGER,
    -- 是否包含商品ID标志
    has_product_id          INTEGER,
    -- 验证票据（用于获取临时素材时使用）
    ticket                  TEXT,
    -- 图片高度（图片消息时有效）
    img_height              INTEGER,
    -- 图片宽度（图片消息时有效）
    img_width               INTEGER,
    -- 子消息类型
    sub_msg_type            INTEGER,
    -- 新消息ID（主要用于撤回/重发消息时使用）
    new_msg_id              INTEGER,
    -- 原始内容（用于撤回/编辑消息时使用）
    ori_content             TEXT,
    -- 加密后的文件名（加密消息时有效）
    encr_file_name          TEXT,
    -- 创建时间
    created_time            DATETIME,
    -- 更新时间
    updated_time            DATETIME
);

CREATE TABLE IF NOT EXISTS wechat_message_handler_config
(
    -- 用户名称
    user_name    TEXT PRIMARY KEY,
    -- 消息处理函数
    handler_name TEXT NOT NULL,
    -- 创建时间
    created_time DATETIME,
    -- 更新时间
    updated_time DATETIME
);