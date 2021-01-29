// Copyright 2021 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.firebase.encoders.proto.codegen

import com.google.protobuf.compiler.PluginProtos.CodeGeneratorRequest
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse
import java.io.PrintWriter
import java.io.StringWriter

/**
 * Main entry point to the executable jar.
 *
 * According to protoc plugin spec, this function is invoked with no arguments and receives a
 * serialized [CodeGeneratorRequest] via `stdin` and must return a [CodeGeneratorResponse] via
 * `stdout`.
 */
fun main(args: Array<String>) {
    val request = CodeGeneratorRequest.parseFrom(System.`in`)
    runCatching {
        CodeGenerator(request).generate().writeTo(System.out)
    }.onFailure {
        val stringWriter = StringWriter()
        it.printStackTrace(PrintWriter(stringWriter))
        CodeGeneratorResponse.newBuilder()
            .setError(stringWriter.toString())
            .build()
            .writeTo(System.out)
    }
}
