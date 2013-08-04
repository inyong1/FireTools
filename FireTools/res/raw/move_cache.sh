#!/system/bin/sh

echo "Script pemindah system dalvik cache"
LOG=/data/local/tmp/move.log

# mkdir /data/sdext2/dalvik-cache_system
cd /data/dalvik-cache

for i in system@app*
do
  if [ "$(ls -l $i | grep ">")" == "" ]; then 
    echo "Memindah... $i"
    busybox cp -pr $i /data/sdext2/dalvik-cache/ 1>>$LOG 2>>$LOG
  fi
done

 for a in system@app*
do
  if [ "$(ls -l $a | grep ">")" == "" ]; then 
    echo "Menghapus... $a"
    rm -r $a 1>>$LOG 2>>$LOG
  fi
done 

cd /data/sdext2/dalvik-cache

for j in system@app*
do
  echo "Membuat link... $j"
  ln -s $j /data/dalvik-cache/$j 1>>$LOG 2>>$LOG
done

#sleep 2
echo ""
echo "Proses selesai"
echo "Harap restart"
echo "--------------"


