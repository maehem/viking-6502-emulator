/*
    Licensed to the Apache Software Foundation (ASF) under one or more
    contributor license agreements.  See the NOTICE file distributed with this
    work for additional information regarding copyright ownership.  The ASF
    licenses this file to you under the Apache License, Version 2.0
    (the "License"); you may not use this file except in compliance with the
    License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
    License for the specific language governing permissions and limitations
    under the License.
 */
package com.maehem.viking.emulator;

/**
 *
 * @author Mark J Koch ( @maehem on GitHub )
 */
class P65C02 {

    private int accumulator;
    private int y;
    private int x;
    private int programCounter;
    private int stackPointer;

    private final int[] mem = new int[0xFFFF];

    private ProcessorStatusRegister P;

    public enum Mode {
        A, // Absolute
        AX, AY, // Absolute Indexed Indirect
        AIX, AIY, // Abs. Indexed with X/Y
        AI, // Abs. Indirect
        IM, // Immediate,
        ACA, // Accumulator A
        IMA, // Immediate Addressing
        IMP, // Implied
        PCR, // Program Counter Relative
        STK, // Stack
        ZP, // Zero Page
        ZPII, // Zero page Indexed Indirect
        ZPX, // Zero page Indexed with X
        ZPY, // Zero page Indexed with Y
        ZPI, // Zero Page Indirect
        ZPIY // Zero Page Indirect Indexed with Y
    }

    /**
     * <pre>
     * ADC (ADd with Carry)
     * Affects Flags: N V Z C
     *
     * MODE           SYNTAX       HEX LEN TIM
     * Immediate     ADC #$44      $69  2   2
     * Zero Page     ADC $44       $65  2   3
     * Zero Page,X   ADC $44,X     $75  2   4
     * Absolute      ADC $4400     $6D  3   4
     * Absolute,X    ADC $4400,X   $7D  3   4+
     * Absolute,Y    ADC $4400,Y   $79  3   4+
     * Indirect,X    ADC ($44,X)   $61  2   6
     * Indirect,Y    ADC ($44),Y   $71  2   5+
     *
     * + add 1 cycle if page boundary crossed
     *
     * ADC results are dependant on the setting of the decimal flag.
     * In decimal mode, addition is carried out on the assumption that
     * the values involved are packed BCD (Binary Coded Decimal).
     *
     * There is no way to add without carry.
     *
     * If N is set already, then adding may or may not cause the
     * result to still be negative.
     *
     * </pre>
     */
    public void adc(int val, Mode m) {
        switch ( m ) {
            case IM -> { // val is value
                accumulator += val & 0xFF;
                if (accumulator > 0xFF) {
                    P.CARRY = true;
                }
                P.CARRY = accumulator > 0xFF;
                accumulator &= 0xFF;
            }
            case ZP, A -> { // val is addr
                accumulator += mem[val] & 0xFF;
                if (accumulator > 0xFF) {
                    P.CARRY = true;
                }
                P.CARRY = accumulator > 0xFF;
                accumulator &= 0xFF;
            }
            case ZPX, AIX -> { // val is addr
                x += mem[val] & 0xFF;
                if (x > 0xFF) {
                    P.CARRY = true;
                }
                P.CARRY = x > 0xFF;
                x &= 0xFF;
            }
            case ZPY -> { // val is addr
                y += mem[val] & 0xFF;
                if (x > 0xFF) {
                    P.CARRY = true;
                }
                P.CARRY = y > 0xFF;
                y &= 0xFF;
            }
        }
    }

    /**
     * <pre>
     * AND (bitwise AND with accumulator)
     * Affects Flags: N Z
     *
     * MODE           SYNTAX       HEX LEN TIM
     * Immediate     AND #$44      $29  2   2
     * Zero Page     AND $44       $25  2   3
     * Zero Page,X   AND $44,X     $35  2   4
     * Absolute      AND $4400     $2D  3   4
     * Absolute,X    AND $4400,X   $3D  3   4+
     * Absolute,Y    AND $4400,Y   $39  3   4+
     * Indirect,X    AND ($44,X)   $21  2   6
     * Indirect,Y    AND ($44),Y   $31  2   5+
     *
     * + add 1 cycle if page boundary crossed
     * </pre>
     */
    public void and(String args) {

    }

    /**
     *
     * <pre>
     * ASL (Arithmetic Shift Left)
     * Affects Flags: N Z C
     *
     * MODE           SYNTAX       HEX LEN TIM
     * Accumulator   ASL A         $0A  1   2
     * Zero Page     ASL $44       $06  2   5
     * Zero Page,X   ASL $44,X     $16  2   6
     * Absolute      ASL $4400     $0E  3   6
     * Absolute,X    ASL $4400,X   $1E  3   7
     *
     * ASL shifts all bits left one position. 0 is shifted into bit 0 and the original bit 7 is shifted into the Carry.
     * </pre>
     */
    public void asl() { // Accumulator

    }

    public void asl(Mode m, int addr) {

    }

    public void aslx(int addr) {

    }

    /**
     * <pre>
     *
     * BIT (test BITs)
     * Affects Flags: N V Z
     *
     * MODE           SYNTAX       HEX LEN TIM
     * Zero Page     BIT $44       $24  2   3
     * Absolute      BIT $4400     $2C  3   4
     *
     * BIT sets the Z flag as though the value in the address tested were ANDed with the accumulator. The N and V flags are set to match bits 7 and 6 respectively in the value stored at the tested address.
     * BIT is often used to skip one or two following bytes as in:
     *
     * CLOSE1 LDX #$10   If entered here, we
     * .BYTE $2C  effectively perform
     * CLOSE2 LDX #$20   a BIT test on $20A2,
     * .BYTE $2C  another one on $30A2,
     * CLOSE3 LDX #$30   and end up with the X
     * CLOSEX LDA #12    register still at $10
     * STA ICCOM,X upon arrival here.
     *
     * Beware: a BIT instruction used in this way as a NOP does have effects: the flags may be modified, and the read of the absolute address, if it happens to access an I/O device, may cause an unwanted action.
     * </pre>
     */
    public void bcc(int addr) {
        if (P.CARRY) {
            programCounter = addr;
        }
    }

    public void clc() {
        P.CARRY = false;
    }

    public void cld() {
        P.DEC = false;
    }

    public void lda(Mode m, int val) {
        accumulator = val & 0xFF;
    }

    public void ldx(int val) {
        X = val & 0xFF;
    }

    public void ldy(int val) {
        Y = val & 0xFF;
    }

    public void lsr(int addr) {
        P.CARRY = (mem[addr] & 0x01) > 0;

        mem[addr] >>= 1;
    }

    /**
     * Shift left, move carry to LSB
     *
     * @param addr
     */
    public void rol(int addr) {
        mem[addr] <<= 1;
        if (mem[addr] > 0xFF) {
            mem[addr] |= 0x01;
            mem[addr] &= 0xFF;
        }

    }

    public void sta(Mode m, int addr) {
        mem[addr] = accumulator;
    }

}
