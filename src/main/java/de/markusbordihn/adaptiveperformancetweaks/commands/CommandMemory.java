/**
 * Copyright 2021 Markus Bordihn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package de.markusbordihn.adaptiveperformancetweaks.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

import de.markusbordihn.adaptiveperformancetweaks.system.MemoryInfo;
import de.markusbordihn.adaptiveperformancetweaks.system.MemoryManager;

public class CommandMemory implements Command<CommandSource> {

  private static final CommandMemory command = new CommandMemory();

  public static ArgumentBuilder<CommandSource, ?> register() {
    return Commands.literal("memory").requires(cs -> cs.hasPermissionLevel(2)).executes(command);
  }

  @Override
  public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
    MemoryInfo memoryInfo = MemoryManager.getMemoryUsage();
    StringBuilder memoryOverview = new StringBuilder(String.format("Memory Overview\n==="));
    memoryOverview.append(String.format("► Initial memory: %.2f MB\n", memoryInfo.getInit()));
    memoryOverview.append(String.format("► Used heap memory: %.2f MB\n", memoryInfo.getUsed()));
    memoryOverview.append(String.format("► Max heap memory: %.2f MB\n", memoryInfo.getMax()));
    memoryOverview
        .append(String.format("► Committed memory: %.2f MB\n", memoryInfo.getCommitted()));
    memoryOverview.append(
        String.format("► Free memory: %.2f MB (%.2f%%)", memoryInfo.getMax() - memoryInfo.getUsed(),
            100 - ((100 * memoryInfo.getUsed()) / memoryInfo.getMax())));
    context.getSource().sendFeedback(new StringTextComponent(memoryOverview.toString()), false);
    return 0;
  }
}
