/*
 * Copyright 2000-2014 JetBrains s.r.o.
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
 */

/*
 * Created by IntelliJ IDEA.
 * User: max
 * Date: May 13, 2002
 * Time: 3:16:36 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package com.intellij.openapi.editor.actions;

import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.CaretAction;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorAction;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.actionSystem.DataContext;
import org.jetbrains.annotations.Nullable;

public class PageUpWithSelectionAction extends EditorAction {
  public static class Handler extends EditorActionHandler {
    @Override
    public void execute(final Editor editor, @Nullable Caret caret, DataContext dataContext) {
      if (!editor.getCaretModel().supportsMultipleCarets()) {
        EditorActionUtil.moveCaretPageUp(editor, true);
        return;
      }
      if (editor.isColumnMode()) {
        int lines = editor.getScrollingModel().getVisibleArea().height / editor.getLineHeight();
        Caret currentCaret = caret == null ? editor.getCaretModel().getPrimaryCaret() : caret;
        for (int i = 0; i < lines; i++) {
          if (!EditorActionUtil.cloneOrRemoveCaret(editor, currentCaret, true)) {
            break;
          }
          currentCaret = editor.getCaretModel().getPrimaryCaret();
        }
      }
      else {
        if (caret == null) {
          editor.getCaretModel().runForEachCaret(new CaretAction() {
            @Override
            public void perform(Caret caret) {
              EditorActionUtil.moveCaretPageUp(editor, true);
            }
          });
        }
        else {
          // assuming caret is equal to CaretModel.getCurrentCaret()
          EditorActionUtil.moveCaretPageUp(editor, true);
        }
      }
    }
  }

  public PageUpWithSelectionAction() {
    super(new Handler());
  }
}
