package so.mp3.type;

public class Mp3 {
	
	private String title;
	private String singer;
	private String album;
	private String link;
	private String size;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSinger() {
		return singer;
	}
	public void setSinger(String singer) {
		this.singer = singer;
	}
	public String getAlbum() {
		return album;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	@Override
	public String toString() {
		return "Mp3 [title=" + title + ", singer=" + singer + ", album="
				+ album + ", link=" + link + ", size=" + size + "]";
	}
}
