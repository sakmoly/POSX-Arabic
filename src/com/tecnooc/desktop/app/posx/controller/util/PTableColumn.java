/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tecnooc.desktop.app.posx.controller.util;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableView;

/**
 *
 * @author noufal
 */
public class PTableColumn<S, T> extends javafx.scene.control.TableColumn<S, T> {

    private final DoubleProperty percentageWidth = new SimpleDoubleProperty(1);
    private final DoubleProperty pixelWidth = new SimpleDoubleProperty(1);

    public PTableColumn() {
        tableViewProperty().addListener(new ChangeListener<TableView<S>>() {
            @Override
            public void changed(ObservableValue<? extends TableView<S>> ov, TableView<S> t, TableView<S> t1) {
                if (PTableColumn.this.prefWidthProperty().isBound()) {
                    PTableColumn.this.prefWidthProperty().unbind();
                }

                PTableColumn.this.prefWidthProperty().bind(pixelWidth);
                if (t1 != null) {
                    t1.widthProperty().addListener(new ChangeListener<Number>() {
                        @Override
                        public void changed(ObservableValue<? extends Number> ov, Number oldWidth, Number newWidth) {
                            if (newWidth != null) {
                                double actualWidth = newWidth.doubleValue() - 17;
                                double columnWidth = (actualWidth * percentageWidth.doubleValue());
                                pixelWidth.set(columnWidth);
                            }
                        }
                    });
                }
            }
        });
    }

    public final DoubleProperty percentageWidthProperty() {
        return this.percentageWidth;
    }

    public final double getPercentageWidth() {
        return this.percentageWidthProperty().get();
    }

    public final void setPercentageWidth(double value) throws IllegalArgumentException {
        if (value >= 0 && value <= 1) {
            this.percentageWidthProperty().set(value);
        } else {
            throw new IllegalArgumentException(String.format("The provided percentage width is not between 0.0 and 1.0. Value is: %1$s", value));
        }
    }
}