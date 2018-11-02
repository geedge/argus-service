#!/usr/bin/env bash
# /**
#  * Copyright (c)  All rights reserved.
#  *
#  * Author: darnell
#  *
#  */
echo ""
echo "[info] rebuilding your DB and clearing data......"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[INFO] This operation will clear your data and restore initial state"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[WARN] Do you confirm the continuation ? Otherwise, please close the window（5）"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[WARN] Do you confirm the continuation ? Otherwise, please close the window（4）"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[WARN] Do you confirm the continuation ? Otherwise, please close the window（3）"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[WARN] Do you confirm the continuation ? Otherwise, please close the window（2）"
read -s -n1 -p "请按任意键继续 ... "
echo ""
echo "[WARN] Do you confirm the continuation ? Otherwise, please close the window。（1）"
read -s -n1 -p "请按任意键继续 ... "
echo ""

cd ..
mvn antrun:run -Pinit-db
