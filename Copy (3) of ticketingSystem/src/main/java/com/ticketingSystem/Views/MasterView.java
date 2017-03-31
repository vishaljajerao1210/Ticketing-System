package com.ticketingSystem.Views;

import com.ticketingSystem.Views.TicketView.PageView;

public class MasterView  {
 public interface CategoryView  extends PageView,MasterDataView{}
 
 public interface DomainView extends DomainFieldView,PageView,MasterDataView{}

 public interface SubDomainView extends SubDomainFieldView,PageView,MasterDataView{}
 
 public interface DomainFieldView extends ListMasterView,PageView ,MasterDataView{}
 
 public interface ListMasterView extends PageView,MasterDataView{}
 
 public interface ListValueView  extends PageView,MasterDataView{}
 
 public interface SubDomainFieldView  extends ListMasterView,DomainFieldView,PageView,MasterDataView{}
 

 public interface MasterDataView{}

 
 public interface TicketDetailView extends TicketMasterView{}
 
 public interface TicketMasterView extends PageView{}
 
 public interface UserView{}
 
 public interface UserGroupView{}
 
 public interface UserManagerView extends UserProfileView{}
 
 public interface ApproverDetailsView{}
 
 
 public interface FileUploadView{}
 
 public interface FileItemView{}
 
 public interface UserProfileView{}
 
 public interface SubDomainExpertView extends SubDomainView,UserProfileView,MasterDataView{}
 
 public interface StatusView{}
 
}
