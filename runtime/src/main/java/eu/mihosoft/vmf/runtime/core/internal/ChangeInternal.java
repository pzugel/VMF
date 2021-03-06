package eu.mihosoft.vmf.runtime.core.internal;

import eu.mihosoft.vcollections.VListChangeEvent;
import eu.mihosoft.vmf.runtime.core.Change;
import vjavax.observer.collection.CollectionChangeEvent;

/**
 * For internal use by the VMF framework. Do not rely on this API. Seriously, don't rely on it.
 * It might change without prior notice.
 **/  
@Deprecated
public interface ChangeInternal extends Change {

  static final String CHANGE_TYPE_CROSS_REF   = "vmf:change:type:crossref";
  static final String CHANGE_TYPE_CONTAINMENT = "vmf:change:type:containment";

  /**
   * A string that indicates additional information, such as events generated by cross-ref etc.
   */
  String getInternalChangeInfo();

  /**
   * Indicates whether the specified change is only a crossref change, i.e, triggered by setting the opposite
   * of the cross ref.
   * @param c change to check
   * @return {@code true} if the specified change is triggered by setting the opposite; {@code false} otherwise
   */
  static boolean isCrossRefChange(Change c) {
    if(c instanceof ChangeInternal) {
      String changeInfo = ((ChangeInternal)c).getInternalChangeInfo();

      return CHANGE_TYPE_CROSS_REF.equals(changeInfo);
    }

    return false;
  }

    /**
   * Indicates whether the specified change is only a crossref list change, i.e, triggered by setting the opposite
   * of the cross ref.
   * @param c change to check
   * @return {@code true} if the specified change is triggered by setting the opposite; {@code false} otherwise
   */
  static boolean isCrossRefChange(CollectionChangeEvent c) {

    return CHANGE_TYPE_CROSS_REF.equals(c.eventInfo());
  }
}