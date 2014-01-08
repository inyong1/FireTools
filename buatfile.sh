

c=100
while [ "$c" -lt "118" ]; do
echo $c
echo "<?xml version=\"1.0\" encoding=\"utf-8\"?>">>c"$c".xml
echo "<animation-list">>c"$c".xml
echo "	android:oneshot=\"false\"">>c"$c".xml
echo "	xmlns:android=\"http://schemas.android.com/apk/res/android\">">>c"$c".xml

echo "	<item android:duration=\"1000\">">>c"$c".xml
echo "		<layer-list>">>c"$c".xml
echo "			<item android:drawable=\"@drawable/b"$c"\"/>">>c"$c".xml
echo "			<item android:drawable=\"@drawable/petir\"/>">>c"$c".xml
echo "		</layer-list>">>c"$c".xml
echo "	</item>">>c"$c".xml
echo "	<item android:duration=\"500\"">>c"$c".xml
echo "		android:drawable=\"@drawable/b"$c"\"/>">>c"$c".xml

echo "</animation-list>">>c"$c".xml
 
 c=$(( "$c" + 1))
 done
 

