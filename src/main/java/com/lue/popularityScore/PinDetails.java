package com.lue.popularityScore;

public class PinDetails {

    private String id;

    private PinImage image;

    private Counts counts;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public PinImage getImage ()
    {
        return image;
    }

    public void setImage (PinImage image)
    {
        this.image = image;
    }

    public Counts getCounts ()
    {
        return counts;
    }

    public void setCounts (Counts counts)
    {
        this.counts = counts;
    }

	@Override
	public String toString() {
		return "PinDetails [id=" + id + ", image=" + image + ", counts="
				+ counts + "]";
	}
}
		
