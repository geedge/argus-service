-- Add/modify columns 
alter table DNS_RESPONSE_STRATEGY add RES_GROUP_3_ID INTEGER default 0;
-- Add comments to the columns 
comment on column DNS_RESPONSE_STRATEGY.RES_GROUP_3_ID
  is 'DNS_GROUP_TYPE（GROUP_ID），default(0)';
﻿-- Add/modify columns 
alter table DNS_RESPONSE_STRATEGY add RES_GROUP_3_NUM INTEGER default 0;
-- Add comments to the columns 
comment on column DNS_RESPONSE_STRATEGY.RES_GROUP_3_NUM
  is 'default(0)';
﻿-- Add/modify columns 
alter table DNS_RESPONSE_STRATEGY add RES_GROUP_4_ID INTEGER default 0;
-- Add comments to the columns 
comment on column DNS_RESPONSE_STRATEGY.RES_GROUP_4_ID
  is 'DNS_GROUP_TYPE（GROUP_ID），default(0)';
﻿-- Add/modify columns 
alter table DNS_RESPONSE_STRATEGY add RES_GROUP_4_NUM INTEGER default 0;
-- Add comments to the columns 
comment on column DNS_RESPONSE_STRATEGY.RES_GROUP_4_NUM
  is 'default(0)';
﻿-- Add/modify columns 
alter table DNS_RESPONSE_STRATEGY add RES_GROUP_5_ID INTEGER default 0;
-- Add comments to the columns 
comment on column DNS_RESPONSE_STRATEGY.RES_GROUP_5_ID
  is 'DNS_GROUP_TYPE（GROUP_ID），default(0)';
﻿-- Add/modify columns 
alter table DNS_RESPONSE_STRATEGY add RES_GROUP_5_NUM INTEGER default 0;
-- Add comments to the columns 
comment on column DNS_RESPONSE_STRATEGY.RES_GROUP_5_NUM
  is 'default(0)';

