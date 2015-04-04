/*
 * Copyright 2010-2015 JetBrains s.r.o.
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

package org.jetbrains.kotlin.codegen.intrinsics

import com.intellij.psi.PsiElement
import org.jetbrains.kotlin.codegen.*
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.kotlin.codegen.AsmUtil.genToString
import org.jetbrains.kotlin.codegen.context.CodegenContext
import org.jetbrains.kotlin.codegen.state.GenerationState
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.psi.JetExpression

public class ToString : LazyIntrinsicMethod() {
    override fun generateImpl(
            codegen: ExpressionCodegen,
            returnType: Type,
            element: PsiElement?,
            arguments: List<JetExpression>,
            receiver: StackValue
    ): StackValue {
        return genToString(receiver, receiver.type)
    }

    override fun toCallable(method: CallableMethod): ExtendedCallable {
        val type = AsmUtil.stringValueOfType(method.getThisType() ?: method.getReceiverClass())
        return UnaryIntrinsic(method, newThisType = type) {
            it.invokestatic("java/lang/String", "valueOf", "(" + type.getDescriptor() + ")Ljava/lang/String;", false)
        }
    }
}
