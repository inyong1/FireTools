package inyong.xt530.tools.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;
import inyong.xt530.tools.*;
import java.util.*;

public class ListAppBaseAdapterKu extends BaseAdapter
{
	private static ArrayList<ItemDetils> arrayListItemDetils;
	private LayoutInflater layoutInflater;

	public ListAppBaseAdapterKu(Context context, ArrayList<ItemDetils> arrayListItemDetils)
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
		TextView namaApp;
		TextView sizeData;
		TextView statusOninternal;
		TextView statusTerlink;
		TextView folderData;
		ImageView iconApp;
	}

	public View getView(int posisi, View view, ViewGroup parentView)
	{
		ViewHolder holder;

		if (view == null)
		{
			view = layoutInflater.inflate(R.layout.row_listview_create_link_data, null);

			holder = new ViewHolder();

			holder.namaApp = (TextView) view.findViewById(R.id.id_tv_row_lv_createlinkdata_namaaplikasi);
			holder.sizeData =(TextView) view.findViewById(R.id.id_tv_row_lv_createlinkdata_ukurandata);
			holder.statusOninternal=(TextView)view.findViewById(R.id.id_tv_row_lv_createlinkdata_status_oninternal);
			holder.statusTerlink=(TextView)view.findViewById(R.id.id_tv_row_lv_createlinkdata_status_terlink);
			holder.folderData = (TextView) view.findViewById(R.id.id_tv_row_lv_createlinkdata_folderdata);
			holder.iconApp = (ImageView) view.findViewById(R.id.id_imageview_createlinkdata_iconapp);

			view.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) view.getTag();
		}
		ItemDetils itemDetil =arrayListItemDetils.get(posisi);
		holder.namaApp.setText( itemDetil.getNamaAplikasi());
		holder.sizeData.setText(itemDetil.getSizeFolderData());
		holder.statusOninternal.setText(itemDetil.getStatusOnInternal());
		holder.statusTerlink.setText(itemDetil.getStatusTerlink());
		holder.folderData.setText(itemDetil.getFolderData());
		holder.iconApp.setImageDrawable(itemDetil.getIcon());

		return view;
	}

}
