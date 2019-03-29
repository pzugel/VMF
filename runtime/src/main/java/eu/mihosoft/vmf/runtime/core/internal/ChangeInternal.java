package eu.mihosoft.vmf.runtime.core.internal;

import eu.mihosoft.vmf.runtime.core.Change;

/**
 * For internal use by the VMF framework. Do not rely on this API. Seriously, don't rely on it.
 * It might change without prior notice.
 **/  
@Deprecated
public interface ChangeInternal extends Change {

  /**
   * A string that indicates additional information, such as events generated by cross-ref etc.
   */
  String getInternalChangeInfo();
}