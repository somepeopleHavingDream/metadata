#!/bin/bash

A=`ps -C nginx --no-header | wc -l`
# 判断 nginx 是否宕机，如果宕机了，尝试重启
if [ $A -eq 0 ]; then
  systemctl start nginx
  # 等待一小会再次检查 nginx ，如果没有启动成功，则停止 keepalived ，使其启动备用机
  sleep 3
  if [ `ps -C nginx --no-header | wc -l` -eq 0 ]; then
    systemctl stop keepalived
  fi
fi