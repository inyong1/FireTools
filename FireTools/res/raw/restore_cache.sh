#!/system/bin/sh
LOG=/data/local/tmp/restore_cache.log

echo "Restore cache..."
# cd /data/dalvik-cache

# for i in system@app*
# do
#  echo "menghapus $i link" 1>>$LOG 2>>$LOG
#  rm $i 1>>$LOG 2>>$LOG
# done

cd /data/sdext2/dalvik-cache

for j in system@app*
do
  echo "restore cache $j"
  busybox cp -pr $j /data/dalvik-cache/
done

rm -r system@app*

echo "hh mohon di reboot 2x"
echo "tataaa......  nih"
#sleep 3
