package org.jj;

import javax.swing.*;
import javax.swing.text.*;

public class NumericTextField extends JTextField {
    private final int decimalPlaces; // Max decimal places allowed

    public NumericTextField(int columns, int decimalPlaces) {
        super(columns);
        this.decimalPlaces = decimalPlaces;
        ((AbstractDocument) this.getDocument()).setDocumentFilter(new NumericDocumentFilter(decimalPlaces));
    }

    static class NumericDocumentFilter extends DocumentFilter {
        private final int decimalPlaces;

        public NumericDocumentFilter(int decimalPlaces) {
            this.decimalPlaces = decimalPlaces;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), string, offset)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (isValidInput(fb.getDocument().getText(0, fb.getDocument().getLength()), text, offset)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        private boolean isValidInput(String currentText, String newText, int offset) {
            String fullText = new StringBuilder(currentText).insert(offset, newText).toString();

            // Allow empty input for easy editing
            if (fullText.isEmpty()) return true;

            // If decimals are not allowed, only allow whole numbers
            if (decimalPlaces == 0) return fullText.matches("\\d*");

            // Allow numbers with at most one decimal point and limited decimal places
            if (!fullText.matches("\\d*\\.?\\d*")) return false;

            // Ensure the number of decimal places does not exceed the allowed limit
            if (fullText.contains(".")) {
                int decimalIndex = fullText.indexOf(".");
                int decimalsAfterPoint = fullText.length() - decimalIndex - 1;
                if (decimalsAfterPoint > decimalPlaces) {
                    return false;
                }
            }

            return true;
        }
    }
}


