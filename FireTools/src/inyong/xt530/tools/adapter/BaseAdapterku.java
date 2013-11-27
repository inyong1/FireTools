package inyong.xt530.tools.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import inyong.xt530.tools.*;
import java.util.*;

public class BaseAdapterku extends BaseAdapter
{
	private static ArrayList<ItemDetils> arrayListItemDetils;
	private LayoutInflater layoutInflater;

	public BaseAdapterku(Context context, ArrayList<ItemDetils> arrayListItemDetils)
	{
		layoutInflater = LayoutInflater.from(context);
		this.arrayListItemDetils = arrayListItemDetils;
	}

	public int getCount()
	{
		return arrayListItemDetils.size();
	}

	public Object getItem(int p1)
	{
		return arrayListItemDetils.get(p1);
	}

	public long getItemId(int p1)
	{
		return p1;
	}

	static class ViewHolder
	{
		TextView judul;
		TextView rincian;
		TextView total;
	}

	public View getView(int p1, View p2, ViewGroup p3)
	{
		ViewHolder holder;
		
		if (p2 == null)
		{
			p2 = layoutInflater.inflate(R.layout.row_listview_daftar_file_nandroid_backup, null);
			
			holder = new ViewHolder();
			
			holder.judul = (TextView) p2.findViewById(R.id.id_tv_row_lv_daftarfilenandroidbackup_judul);
			holder.rincian = (TextView) p2.findViewById(R.id.id_tv_row_lv_daftarfilenandroidbackup_rincian);
			holder.total = (TextView) p2.findViewById(R.id.id_tv_row_lv_daftarfilenandroidbackup_total);
			
			p2.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) p2.getTag();
		}
		
		holder.judul.setText(arrayListItemDetils.get(p1).getJudul());
		holder.rincian.setText(arrayListItemDetils.get(p1).getRincian());
		holder.total.setText(arrayListItemDetils.get(p1).getTotal());
		
		return p2;
	}

}
