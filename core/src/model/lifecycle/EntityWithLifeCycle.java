package model.lifecycle;

public interface EntityWithLifeCycle {
	public void setPrePersist(PrePersist value);
	public void setPostPersist(PostPersist value);
	public void setPreRemove(PreRemove value);
	public void setPostRemove(PostRemove value);
	public void setPreUpdate(PreUpdate value);
	public void setPostUpdate(PostUpdate value);
	public void setPostLoad(PostLoad value);
}
