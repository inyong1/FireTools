package inyong.xt530.tools.adapter;
import android.graphics.drawable.*;

public class ItemDetils
{
// untuk list file nandroid backup
	private String judul;
	private  String rincian ="";
	private String total;

	public void setTotal(String total)
	{
		this.total = total;
	}

	public String getTotal()
	{
		return total;
	}



	public  void setJudul(String judul)
	{
		this.judul = judul;
	}

	public  String getJudul()
	{
		return judul;
	}


	public  void setRincian(String rincian)
	{
		this.rincian = rincian;
	}

	public  String getRincian()
	{
		return rincian;
	}

	public void appendRincian(String s)
	{
		if (rincian == "")
		{
			rincian = s;

		}
		else
		{
			rincian += "\n" + s;
		}
	}

	// detils untuk list aplikasi pada fungsi create link data
	private String namaAplikasi, namaFolderData, folderData, sizeFolderData, statusOnInternal, statusTerlink;
//	private long size;
//	private boolean linked;
	private Drawable icon;
	
	public void setNamaAplikasi(String namaAplikasi)
	{
		this.namaAplikasi = namaAplikasi;
	}

	public String getNamaAplikasi()
	{
		return namaAplikasi;
	}

	public void setNamaFolderData(String namaFolderData)
	{
		this.namaFolderData = namaFolderData;
	}

	public String getNamaFolderData()
	{
		return namaFolderData;
	}

	public void setFolderData(String folderData)
	{
		this.folderData = folderData;
	}

	public String getFolderData()
	{
		return folderData;
	}
	public void setSizeFolderData(String sizeFolderData)
	{
		this.sizeFolderData = sizeFolderData;
	}

	public String getSizeFolderData()
	{
		return sizeFolderData;
	}

	public void setStatusOnInternal(String statusOnInternal)
	{
		this.statusOnInternal = statusOnInternal;
	}

	public String getStatusOnInternal()
	{
		return statusOnInternal;
	}

	public void setStatusTerlink(String statusTerlink)
	{
		this.statusTerlink = statusTerlink;
	}

	public String getStatusTerlink()
	{
		return statusTerlink;
	}
	public void setIcon(Drawable icon)
	{
		this.icon = icon;
	}

	public Drawable getIcon()
	{
		return icon;
	}
}
