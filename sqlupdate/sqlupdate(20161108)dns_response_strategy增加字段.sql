-- Add/modify columns 
alter table DNS_RESPONSE_STRATEGY add MIN_TTL INTEGER not null;
-- Add comments to the columns 
comment on column DNS_RESPONSE_STRATEGY.MIN_TTL
  is '最小生存时间';
﻿-- Add/modify columns 
alter table DNS_RESPONSE_STRATEGY add MAX_TTL INTEGER not null;
-- Add comments to the columns 
comment on column DNS_RESPONSE_STRATEGY.MAX_TTL
  is '最大生存时间';
