/*
 * Copyright 2016 Hannes Dorfmann.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package android.support.v4.app;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * With this utility class one can access the fragment backstack
 *
 * @author Hannes Dorfmann
 */
public class BackstackAccessor {

  private BackstackAccessor() {
    throw new IllegalStateException("Not instantiateable");
  }

  /**
   * Checks whether or not a given fragment is on the backstack of the fragment manager (could also
   * be on top of the backstack and hence visible)
   *
   * @param fragment The fragment you want to check if its on the backstack
   * @return true, if the given Fragment is on the backstack, otherwise false (not on the backstack)
   */
  public static boolean isFragmentOnBackStack(Fragment fragment) {
    FragmentManager fragmentManager = fragment.getFragmentManager();
    int backStackSize = fragmentManager.getBackStackEntryCount();
    for (int i = 0; i < backStackSize; i++) {
      BackStackRecord stackEntry = (BackStackRecord) fragmentManager.getBackStackEntryAt(i);
      BackStackRecord.Op head = stackEntry.mHead;
      BackStackRecord.Op tail = stackEntry.mTail;
      if (head != null && head.fragment == fragment) {
        return true;
      }

      if (tail != null && tail.fragment == fragment) {
        return true;
      }

      if (head != null && findNext(fragment, head.next)) {
        return true;
      }

      if (head != null && findPrevious(fragment, head.prev)) {
        return true;
      }

      if (tail != null && findNext(fragment, tail.next)) {
        return true;
      }

      if (tail != null && findPrevious(fragment, tail.prev)) {
        return true;
      }
    }

    return false;
  }

  private static boolean findNext(@NonNull Fragment toFind, @Nullable BackStackRecord.Op next) {
    if (next == null) {
      return false;
    }

    if (toFind == next.fragment) {
      return true;
    } else {
      return findNext(toFind, next.next);
    }
  }

  private static boolean findPrevious(@NonNull Fragment toFind,
      @Nullable BackStackRecord.Op previous) {
    if (previous == null) {
      return false;
    }

    if (toFind == previous.fragment) {
      return true;
    } else {
      return findPrevious(toFind, previous.prev);
    }
  }
}
