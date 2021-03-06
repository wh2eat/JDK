/*
 * Copyright (c) 2007, 2017, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sun.org.apache.bcel.internal.generic;

import java.io.*;
import com.sun.org.apache.bcel.internal.util.ByteSequence;

/**
 * SIPUSH - Push short
 *
 * <PRE>Stack: ... -&gt; ..., value</PRE>
 *
 * @author  <A HREF="mailto:markus.dahm@berlin.de">M. Dahm</A>
 */
public class SIPUSH extends Instruction implements ConstantPushInstruction {
  private short b;

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  SIPUSH() {}

  public SIPUSH(short b) {
    super(com.sun.org.apache.bcel.internal.Constants.SIPUSH, (short)3);
    this.b = b;
  }

  /**
   * Dump instruction as short code to stream out.
   */
  public void dump(DataOutputStream out) throws IOException {
    super.dump(out);
    out.writeShort(b);
  }

  /**
   * @return mnemonic for instruction
   */
  public String toString(boolean verbose) {
    return super.toString(verbose) + " " + b;
  }

  /**
   * Read needed data (e.g. index) from file.
   */
  protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException
  {
    length = 3;
    b      = bytes.readShort();
  }

  public Number getValue() { return new Integer(b); }

  /** @return Type.SHORT
   */
  public Type getType(ConstantPoolGen cp) {
    return Type.SHORT;
  }

  /**
   * Call corresponding visitor method(s). The order is:
   * Call visitor methods of implemented interfaces first, then
   * call methods according to the class hierarchy in descending order,
   * i.e., the most specific visitXXX() call comes last.
   *
   * @param v Visitor object
   */
  public void accept(Visitor v) {
    v.visitPushInstruction(this);
    v.visitStackProducer(this);
    v.visitTypedInstruction(this);
    v.visitConstantPushInstruction(this);
    v.visitSIPUSH(this);
  }
}
