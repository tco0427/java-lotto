package lotto.controller;

import java.util.List;
import lotto.domain.Money;
import lotto.domain.LottoMachine;
import lotto.domain.LottoResult;
import lotto.domain.LottoTickets;
import lotto.domain.WinningNumbers;
import lotto.dto.LottoTicketsDto;
import lotto.view.InputView;
import lotto.view.OutputView;

public class LottoController {

    private final InputView inputView;
    private final OutputView outputView;

    public LottoController(InputView inputView, OutputView outputView) {
        this.inputView = inputView;
        this.outputView = outputView;
    }

    public void run() {
        Money money = createMoney();

        LottoTickets lottoTickets = createLottoTickets(money);
        WinningNumbers winningNumbers = createWinningNumbers();

        LottoResult lottoResult = createLottoResult(lottoTickets, winningNumbers);

        outputView.printYield(lottoResult.getRanks(), lottoResult.calculateYield(money));
    }

    private Money createMoney() {
        try {
            return Money.create(inputView.getMoney());
        } catch(IllegalArgumentException e) {
            outputView.printErrorMessage(e.getMessage());

            return createMoney();
        }
    }

    private LottoTickets createLottoTickets(Money money) {
        LottoMachine lottoMachine = new LottoMachine();
        LottoTickets lottoTickets = lottoMachine.purchase(money);
        LottoTicketsDto lottoTicketsDto = new LottoTicketsDto(lottoTickets);

        outputView.printTotalCount(lottoTickets.totalCount());
        outputView.printLottoTicketsInfo(lottoTicketsDto);

        return lottoTickets;
    }

    private WinningNumbers createWinningNumbers() {
        try {
            List<Integer> winningNumbers = inputView.getNormalWinningNumbers();
            int bonusNumber = inputView.getBonusNumber();

            return new WinningNumbers(winningNumbers, bonusNumber);
        } catch (RuntimeException e) {
            outputView.printErrorMessage(e.getMessage());

            return createWinningNumbers();
        }
    }

    private LottoResult createLottoResult(LottoTickets lottoTickets, WinningNumbers winningNumbers) {
        outputView.printLottoResultMessage();

        LottoResult lottoResult = lottoTickets.determine(winningNumbers);

        return lottoResult;
    }
}
